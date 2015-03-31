/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.github.mdsimmo.pixeldungeon.items.weapon;

import com.github.mdsimmo.pixeldungeon.Badges;
import com.github.mdsimmo.pixeldungeon.actors.Char;
import com.github.mdsimmo.pixeldungeon.actors.hero.Hero;
import com.github.mdsimmo.pixeldungeon.actors.hero.HeroClass;
import com.github.mdsimmo.pixeldungeon.items.Item;
import com.github.mdsimmo.pixeldungeon.items.KindOfWeapon;
import com.github.mdsimmo.pixeldungeon.items.weapon.enchantments.Death;
import com.github.mdsimmo.pixeldungeon.items.weapon.enchantments.Fire;
import com.github.mdsimmo.pixeldungeon.items.weapon.enchantments.Horror;
import com.github.mdsimmo.pixeldungeon.items.weapon.enchantments.Instability;
import com.github.mdsimmo.pixeldungeon.items.weapon.enchantments.Leech;
import com.github.mdsimmo.pixeldungeon.items.weapon.enchantments.Luck;
import com.github.mdsimmo.pixeldungeon.items.weapon.enchantments.Paralysis;
import com.github.mdsimmo.pixeldungeon.items.weapon.enchantments.Poison;
import com.github.mdsimmo.pixeldungeon.items.weapon.enchantments.Shock;
import com.github.mdsimmo.pixeldungeon.items.weapon.enchantments.Slow;
import com.github.mdsimmo.pixeldungeon.items.weapon.enchantments.Tempering;
import com.github.mdsimmo.pixeldungeon.items.weapon.missiles.MissileWeapon;
import com.github.mdsimmo.pixeldungeon.sprites.ItemSprite;
import com.github.mdsimmo.pixeldungeon.utils.GLog;
import com.github.mdsimmo.pixeldungeon.utils.Utils;
import com.github.mdsimmo.utils.Bundlable;
import com.github.mdsimmo.utils.Bundle;
import com.github.mdsimmo.utils.Random;

public class Weapon extends KindOfWeapon {

    private static final int HITS_TO_KNOW = 20;

    private static final String TXT_IDENTIFY =
            "You are now familiar enough with your %s to identify it. It is %s.";
    private static final String TXT_INCOMPATIBLE =
            "Interaction of different types of magic has negated the enchantment on this weapon!";
    private static final String TXT_TO_STRING = "%s :%d";

    public int STR = 10;
    public float ACU = 1;
    public float DLY = 1f;

    public enum Imbue {
        NONE, SPEED, ACCURACY
    }

    public Imbue imbue = Imbue.NONE;

    private int hitsToKnow = HITS_TO_KNOW;

    protected Enchantment enchantment;

    @Override
    public void proc( Char attacker, Char defender, int damage ) {

        if ( enchantment != null ) {
            enchantment.proc( this, attacker, defender, damage );
        }

        if ( !levelKnown ) {
            if ( --hitsToKnow <= 0 ) {
                levelKnown = true;
                GLog.i( TXT_IDENTIFY, name(), toString() );
                Badges.validateItemLevelAquired( this );
            }
        }

        use();
    }

    private static final String UNFAMILIRIARITY = "unfamiliarity";
    private static final String ENCHANTMENT = "enchantment";
    private static final String IMBUE = "imbue";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( UNFAMILIRIARITY, hitsToKnow );
        bundle.put( ENCHANTMENT, enchantment );
        bundle.put( IMBUE, imbue );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        if ( (hitsToKnow = bundle.getInt( UNFAMILIRIARITY )) == 0 ) {
            hitsToKnow = HITS_TO_KNOW;
        }
        enchantment = (Enchantment) bundle.get( ENCHANTMENT );
        imbue = bundle.getEnum( IMBUE, Imbue.class );
    }

    @Override
    public float acuracyFactor( Hero hero ) {

        int encumbrance = STR - hero.STR();

        if ( this instanceof MissileWeapon ) {
            switch ( hero.heroClass ) {
                case WARRIOR:
                    encumbrance += 3;
                    break;
                case HUNTRESS:
                    encumbrance -= 2;
                    break;
                default:
            }
        }

        return
                (encumbrance > 0 ? (float) (ACU / Math.pow( 1.5, encumbrance )) : ACU) *
                        (imbue == Imbue.ACCURACY ? 1.5f : 1.0f);
    }

    @Override
    public float speedFactor( Hero hero ) {

        int encumrance = STR - hero.STR();
        if ( this instanceof MissileWeapon && hero.heroClass == HeroClass.HUNTRESS ) {
            encumrance -= 2;
        }

        return
                (encumrance > 0 ? (float) (DLY * Math.pow( 1.2, encumrance )) : DLY) *
                        (imbue == Imbue.SPEED ? 0.6f : 1.0f);
    }

    @Override
    public int damageRoll( Hero hero ) {

        int damage = super.damageRoll( hero );

        if ( (hero.rangedWeapon != null) == (hero.heroClass == HeroClass.HUNTRESS) ) {
            int exStr = hero.STR() - STR;
            if ( exStr > 0 ) {
                damage += Random.IntRange( 0, exStr );
            }
        }

        return damage;
    }

    public Item upgrade( boolean enchant ) {
        if ( enchantment != null ) {
            if ( !enchant && Random.Int( level ) > 0 ) {
                GLog.w( TXT_INCOMPATIBLE );
                enchant( null );
            }
        } else {
            if ( enchant ) {
                enchant();
            }
        }

        return super.upgrade();
    }

    @Override
    public int maxDurability( int lvl ) {
        return 4 * (lvl < 16 ? 16 - lvl : 1);
    }

    @Override
    public String toString() {
        return levelKnown ? Utils.format( TXT_TO_STRING, super.toString(), STR ) : super.toString();
    }

    @Override
    public String name() {
        return enchantment == null ? super.name() : enchantment.name( super.name() );
    }

    @Override
    public Item random() {
        if ( Random.Float() < 0.4 ) {
            int n = 1;
            if ( Random.Int( 3 ) == 0 ) {
                n++;
                if ( Random.Int( 3 ) == 0 ) {
                    n++;
                }
            }
            if ( Random.Int( 2 ) == 0 ) {
                upgrade( n );
            } else {
                degrade( n );
                cursed = true;
            }
        }
        return this;
    }

    public Weapon enchant( Enchantment ench ) {
        enchantment = ench;
        return this;
    }

    public Weapon enchant() {

        Class<? extends Enchantment> oldEnchantment = enchantment != null ? enchantment.getClass() : null;
        Enchantment ench = Enchantment.random();
        while ( ench.getClass() == oldEnchantment ) {
            ench = Enchantment.random();
        }

        return enchant( ench );
    }

    public boolean isEnchanted() {
        return enchantment != null;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return enchantment != null ? enchantment.glowing() : null;
    }

    public static abstract class Enchantment implements Bundlable {

        private static final Class<?>[] enchants = new Class<?>[]{
                Fire.class, Poison.class, Death.class, Paralysis.class, Leech.class,
                Slow.class, Shock.class, Instability.class, Horror.class, Luck.class,
                Tempering.class};
        private static final float[] chances = new float[]{10, 10, 1, 2, 1, 2, 6, 3, 2, 2, 3};

        public abstract boolean proc( Weapon weapon, Char attacker, Char defender, int damage );

        public String name( String weaponName ) {
            return weaponName;
        }

        @Override
        public void restoreFromBundle( Bundle bundle ) {
        }

        @Override
        public void storeInBundle( Bundle bundle ) {
        }

        public ItemSprite.Glowing glowing() {
            return ItemSprite.Glowing.WHITE;
        }

        @SuppressWarnings("unchecked")
        public static Enchantment random() {
            try {
                return ((Class<Enchantment>) enchants[Random.chances( chances )]).newInstance();
            } catch ( Exception e ) {
                return null;
            }
        }

    }
}

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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>
 */
package com.github.mdsimmo.pixeldungeon.items.food;

import com.github.mdsimmo.noosa.audio.Sample;
import com.github.mdsimmo.pixeldungeon.Assets;
import com.github.mdsimmo.pixeldungeon.Badges;
import com.github.mdsimmo.pixeldungeon.Statistics;
import com.github.mdsimmo.pixeldungeon.actors.buffs.Buff;
import com.github.mdsimmo.pixeldungeon.actors.buffs.Burning;
import com.github.mdsimmo.pixeldungeon.actors.buffs.Hunger;
import com.github.mdsimmo.pixeldungeon.actors.buffs.Paralysis;
import com.github.mdsimmo.pixeldungeon.actors.buffs.Poison;
import com.github.mdsimmo.pixeldungeon.actors.buffs.Roots;
import com.github.mdsimmo.pixeldungeon.actors.buffs.Slow;
import com.github.mdsimmo.pixeldungeon.actors.hero.Hero;
import com.github.mdsimmo.pixeldungeon.effects.Speck;
import com.github.mdsimmo.pixeldungeon.effects.SpellSprite;
import com.github.mdsimmo.pixeldungeon.items.Item;
import com.github.mdsimmo.pixeldungeon.items.scrolls.ScrollOfRecharging;
import com.github.mdsimmo.pixeldungeon.utils.GLog;
import com.github.mdsimmo.utils.Random;

import java.util.ArrayList;

public abstract class Food extends Item {

    public static final float FULL_FILL = Hunger.STARVING;
    public static final float NORMAL = Hunger.HUNGRY;
    public static final float HALF_VALUE = Hunger.STARVING - Hunger.HUNGRY;
    public static final float JUNK_FOOD = HALF_VALUE / 2;
    public static final float VERY_LITTLE = JUNK_FOOD / 5;
    public static final float BAD = -JUNK_FOOD;

    private static final float TIME_TO_EAT = 3f;

    private static final String AC_EAT = "EAT";

    {
        stackable = true;
    }

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add( AC_EAT );
        return actions;
    }

    @Override
    public void execute( Hero hero, String action ) {
        if ( action.equals( AC_EAT ) ) {
            eat( hero );
        } else {

            super.execute( hero, action );

        }
    }

    public void eat( Hero hero ) {
        detach( hero.belongings.backpack );

        hero.buff( Hunger.class ).satisfy( getEnergy() );
        GLog.i( getMessage() );

        switch ( hero.heroClass ) {
            case WARRIOR:
                if ( hero.HP < hero.HT ) {
                    hero.HP = Math.min( hero.HP + 5, hero.HT );
                    hero.sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
                }
                break;
            case MAGE:
                hero.belongings.charge( false );
                ScrollOfRecharging.charge( hero );
                break;
            case ROGUE:
            case HUNTRESS:
                break;
        }

        hero.sprite.operate( hero.pos );
        hero.busy();
        SpellSprite.show( hero, SpellSprite.FOOD );
        Sample.INSTANCE.play( Assets.SND_EAT );

        hero.spend( TIME_TO_EAT );

        Statistics.foodEaten++;
        Badges.validateFoodEaten();
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    public abstract float getEnergy();

    public abstract String getMessage();

    @Override
    public int price() {
        // the energy/10. Minimum of 2
        // ~= 5 to 20 gold
        return Math.max( (int)(quantity()*getEnergy()/10), 2 );
    }

    protected void rawEat( Hero hero ) {
        switch ( Random.Int( 5 ) ) {
            case 0:
                GLog.w( "Oh it's hot!" );
                Buff.affect( hero, Burning.class ).reignite( hero );
                break;
            case 1:
                GLog.w( "You can't feel your legs!" );
                Buff.prolong( hero, Roots.class, Paralysis.duration( hero ) );
                break;
            case 2:
                GLog.w( "You are not feeling well." );
                Buff.affect( hero, Poison.class ).set( Poison.durationFactor( hero ) * hero.HT / 5 );
                break;
            case 3:
                GLog.w( "You are stuffed." );
                Buff.prolong( hero, Slow.class, Slow.duration( hero ) );
                break;
        }
    }
}

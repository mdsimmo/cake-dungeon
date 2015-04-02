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
package com.github.mdsimmo.pixeldungeon.actors.mobs.neutralmob;

import android.util.Log;

import com.github.mdsimmo.pixeldungeon.Dungeon;
import com.github.mdsimmo.pixeldungeon.actors.Char;
import com.github.mdsimmo.pixeldungeon.actors.buffs.Burning;
import com.github.mdsimmo.pixeldungeon.actors.buffs.Frost;
import com.github.mdsimmo.pixeldungeon.actors.buffs.Paralysis;
import com.github.mdsimmo.pixeldungeon.actors.buffs.Poison;
import com.github.mdsimmo.pixeldungeon.actors.buffs.Roots;
import com.github.mdsimmo.pixeldungeon.actors.mobs.Mob;
import com.github.mdsimmo.pixeldungeon.items.food.MysteryMeat;
import com.github.mdsimmo.pixeldungeon.sprites.MotherPigSprite;
import com.github.mdsimmo.utils.Random;

import java.util.HashSet;

public class MotherPig extends Mob {

    {
        name = "mother pig";
        spriteClass = MotherPigSprite.class;

        HT = HP = Dungeon.hero.HT * 2;
        EXP = Dungeon.hero.lvl * 2;

        defenseSkill = Dungeon.hero.attackSkill( this ) * 2;

        loot = MysteryMeat.class;
        lootChance = 1.0f;
    }

    @Override
    protected boolean act() {
        Log.i("Mother pig", "acting " + this );
        boolean r = super.act();
        Log.i("Mother pig", "fin " + r + " " + this);
        return r;

    }

    @Override
    public int dr() {
        return Dungeon.depth + 10;
    }

    @Override
    public String description() {
        return "While pigs are normally very tame, this one looks rather angry; probably because it" +
                " heard her piglet die. Those tusks look like they could do some serious damage." +
                " I would suggest running away.";
    }

    @Override
    public int attackSkill( Char target ) {
        return Dungeon.hero.defenseSkill( this ) * 2;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( Dungeon.hero.lvl+5, Dungeon.hero.lvl*2 );
    }

    @Override
    public String defenseVerb() {
        return "blocked";
    }

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();
    private static final HashSet<Class<?>> RESISTANCES = new HashSet<>();

    static {
        IMMUNITIES.add( Paralysis.class );
        IMMUNITIES.add( Roots.class );
        IMMUNITIES.add( Frost.class );

        RESISTANCES.add( Burning.class );
        RESISTANCES.add( Poison.class );
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }

    @Override
    public HashSet<Class<?>> resistances() {
        return RESISTANCES;
    }
}
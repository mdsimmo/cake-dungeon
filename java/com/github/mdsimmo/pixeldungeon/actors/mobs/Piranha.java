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
package com.github.mdsimmo.pixeldungeon.actors.mobs;

import com.github.mdsimmo.pixeldungeon.Badges;
import com.github.mdsimmo.pixeldungeon.Dungeon;
import com.github.mdsimmo.pixeldungeon.Statistics;
import com.github.mdsimmo.pixeldungeon.actors.Char;
import com.github.mdsimmo.pixeldungeon.actors.blobs.ToxicGas;
import com.github.mdsimmo.pixeldungeon.actors.buffs.Burning;
import com.github.mdsimmo.pixeldungeon.actors.buffs.Frost;
import com.github.mdsimmo.pixeldungeon.actors.buffs.Paralysis;
import com.github.mdsimmo.pixeldungeon.actors.buffs.Roots;
import com.github.mdsimmo.pixeldungeon.items.food.MysteryMeat;
import com.github.mdsimmo.pixeldungeon.levels.Level;
import com.github.mdsimmo.pixeldungeon.sprites.PiranhaSprite;
import com.github.mdsimmo.utils.Random;

import java.util.HashSet;

public class Piranha extends Mob {

    {
        name = "giant piranha";
        spriteClass = PiranhaSprite.class;

        baseSpeed = 2f;

        EXP = 0;
    }

    public Piranha() {
        super();

        HP = HT = 10 + Dungeon.depth * 5;
        defenseSkill = 10 + Dungeon.depth * 2;
    }

    @Override
    protected boolean act() {
        if ( !Level.water[pos] ) {
            die( null );
            return true;
        } else {
            return super.act();
        }
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( Dungeon.depth, 4 + Dungeon.depth * 2 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 20 + Dungeon.depth * 2;
    }

    @Override
    public int dr() {
        return Dungeon.depth;
    }

    @Override
    public void die( Object cause ) {
        Dungeon.level.drop( new MysteryMeat(), pos ).sprite.drop();
        super.die( cause );

        Statistics.piranhasKilled++;
        Badges.validatePiranhasKilled();
    }

    @Override
    public boolean reset() {
        return true;
    }

    @Override
    protected boolean getCloser( int target ) {

        if ( rooted ) {
            return false;
        }

        int step = Dungeon.findPath( this, pos, target,
                Level.water,
                Level.fieldOfView );
        if ( step != -1 ) {
            move( step );
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected boolean getFurther( int target ) {
        int step = Dungeon.flee( this, pos, target,
                Level.water,
                Level.fieldOfView );
        if ( step != -1 ) {
            move( step );
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String description() {
        return
                "These carnivorous fish are not natural inhabitants of underground pools. " +
                        "They were bred specifically to protect flooded treasure vaults.";
    }

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();

    static {
        IMMUNITIES.add( Burning.class );
        IMMUNITIES.add( Paralysis.class );
        IMMUNITIES.add( ToxicGas.class );
        IMMUNITIES.add( Roots.class );
        IMMUNITIES.add( Frost.class );
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }
}

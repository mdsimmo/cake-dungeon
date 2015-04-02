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
import com.github.mdsimmo.pixeldungeon.actors.mobs.Mob;
import com.github.mdsimmo.pixeldungeon.levels.Level;
import com.github.mdsimmo.utils.Random;

public abstract class NeutralMob extends Mob {

    {
        hostile = false;
        FLEEING = new Flee();
    }

    @Override
    protected boolean act() {

        if ( state != SLEEPING )
            state = FLEEING;
        Log.i("NeutralMob", "Acting");


        return super.act();
    }

    protected class Flee extends Fleeing {

        @Override
        protected void nowhereToRun() {
            int nextStep = -1;
            for ( int i = 0; i < 10; i++ ) {
                int n = Level.NEIGHBOURS8[Random.Int( 8 )];
                nextStep = pos + Level.NEIGHBOURS8[Random.Int( 8 )];
                if ( Level.passable[nextStep] )
                    break;
                nextStep = -1;
            }
            if ( nextStep == -1 ) {
                Log.i("NeutralMob", "No next step");
                return;
            }

            Char c = findChar( nextStep );
            if ( c != null ) {
                attack( c );
                Log.i("NeutralMob", "ATTACK!");
            } else {
                move( nextStep );
                Dungeon.observe();
                Log.i( "NeutralMob", "Move " + cooldown() + " " + pos + toString() );
            }

        }
    }
}

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

import com.github.mdsimmo.noosa.audio.Sample;
import com.github.mdsimmo.pixeldungeon.Assets;
import com.github.mdsimmo.pixeldungeon.Dungeon;
import com.github.mdsimmo.pixeldungeon.actors.mobs.Mob;
import com.github.mdsimmo.pixeldungeon.effects.CellEmitter;
import com.github.mdsimmo.pixeldungeon.effects.Speck;
import com.github.mdsimmo.pixeldungeon.items.food.Bacon;
import com.github.mdsimmo.pixeldungeon.scenes.GameScene;
import com.github.mdsimmo.pixeldungeon.sprites.PigSprite;
import com.github.mdsimmo.pixeldungeon.utils.GLog;

public class Pig extends NeutralMob {
    {
        name = "piglet";
        spriteClass = PigSprite.class;

        HP = HT = Dungeon.depth < 5 ? 8 : 4;
        defenseSkill = 5;

        loot = Bacon.class;
        lootChance = 0.5f;

        maxLvl = 5;
        baseSpeed = 2;


    }

    @Override
    public int dr() {
        return 1;
    }

    @Override
    public String description() {
        if ( Dungeon.depth < 5 )
            return "Pigs often wander in from the surface and get stuck down here. They taste great,"
                    + " but are really hard to catch";
        else
            return "It's rare to see a pig so deep in the dungeon. It must have fallen through one "
                    + "of those chasms.";
    }



    @Override
    public void die( Object cause ) {
        super.die( cause );

        // Find the mother
        MotherPig mother = null;
        for ( Mob mob : Dungeon.level.mobs )
            if ( mob instanceof MotherPig ) {
                mother = (MotherPig) mob;
                break;
            }

        // no mother. create her
        if ( mother == null ) {
            mother = new MotherPig();
            mother.pos = Dungeon.level.randomRespawnCell();
            GameScene.add( mother );
        }

        // tell the mother to take revenge
        mother.beckon( pos );
        GLog.w( "The pig squeals echo deep into the dungeon" );
        CellEmitter.center( pos ).start( Speck.factory( Speck.SCREAM ), 0.3f, 3 );
        Sample.INSTANCE.play( Assets.SND_SQUEAL ); //TODO pig squeal
    }
}

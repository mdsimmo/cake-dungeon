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

import com.github.mdsimmo.pixeldungeon.items.food.LambChop;
import com.github.mdsimmo.pixeldungeon.sprites.SheepSprite;

public class Sheep extends NeutralMob {

    {
        name = "sheep";
        spriteClass = SheepSprite.class;
        HP = 2;

        baseSpeed = 0.5f;

        loot = LambChop.class;
        lootChance = 0.5f;
    }

    public float lifespan = -1;
    private Object timeout = new Object();

    @Override
    protected void spend( float time ) {
        // FIXME sheep deaths are very buggy
        super.spend( time );
        if ( lifespan != -1 ) {
            lifespan -= time;
            if ( lifespan <= 0 ) {
                die( timeout );
            }
        }
    }

    @Override
    public void damage( int dmg, Object src ) {
        //TODO think up a fun/hard way to kill a sheep
        super.damage( dmg, src );
    }

    @Override
    public void die( Object cause ) {
        if ( cause == timeout ) {
            // silently fade away
            destroy();
            sprite.die();
        } else {
            // do normal death
            super.die( cause );
        }
    }

    @Override
    public String description() {
        return
                "This is a magic sheep. What's so magical about it? It'll disappear eventually. I hear" +
                        " it'll reward you greatly if you kill it before it disappears, although it" +
                        " can be quite hard to stab past all that wool...";
    }

}

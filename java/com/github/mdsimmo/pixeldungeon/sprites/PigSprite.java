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
package com.github.mdsimmo.pixeldungeon.sprites;

import com.github.mdsimmo.noosa.TextureFilm;
import com.github.mdsimmo.pixeldungeon.Assets;

public class PigSprite extends MobSprite {

    public PigSprite() {
        super();

        texture( Assets.PIG );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 1, true );
        idle.frames( frames, 0 );

        run = new Animation( 10, true );
        run.frames( frames, 0, 2 );

        attack = new Animation( 10, false );
        attack.frames( frames, 3 );

        die = new Animation( 10, false );
        die.frames( frames, 1 );

        play( idle );
    }
}

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

import com.github.mdsimmo.pixeldungeon.actors.hero.Hero;
import com.github.mdsimmo.pixeldungeon.sprites.ItemSpriteSheet;

public class Bacon extends Food {

    {
        name = "raw bacon";
        image = ItemSpriteSheet.BACON;
    }

    @Override
    public void eat( Hero hero ) {
        super.eat( hero );
        rawEat( hero );
    }

    @Override
    public String info() {
        return "A thick juicy cut of bacon. It would be much nicer cooked.";
    }

    @Override
    public float getEnergy() {
        return Food.HALF_VALUE;
    }

    @Override
    public String getMessage() {
        return "Mmm. Bacon...";
    }
}
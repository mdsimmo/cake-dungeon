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
package com.github.mdsimmo.pixeldungeon.items.food;

import com.github.mdsimmo.pixeldungeon.actors.buffs.Hunger;
import com.github.mdsimmo.pixeldungeon.sprites.ItemSpriteSheet;

public class Pasty extends Food {

    public static final float ENERGY = Hunger.STARVING;

	{
		name = "pasty";
		image = ItemSpriteSheet.PASTY;
	}
	
	@Override
	public String info() {
		return "This is authentic Cornish pasty with traditional filling of beef and potato.";
	}
	
	@Override
	public int price() {
		return 20 * quantity;
	}

    @Override
    public float getEnergy() {
        return ENERGY;
    }

    @Override
    public String getMessage() {
        return FoodRation.MESSAGE;
    }
}

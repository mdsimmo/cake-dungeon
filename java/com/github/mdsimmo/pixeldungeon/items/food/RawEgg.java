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

import com.github.mdsimmo.pixeldungeon.actors.buffs.Buff;
import com.github.mdsimmo.pixeldungeon.actors.buffs.Poison;
import com.github.mdsimmo.pixeldungeon.actors.hero.Hero;
import com.github.mdsimmo.pixeldungeon.sprites.ItemSpriteSheet;

public class RawEgg extends Food {

    public static final String MESSAGE = "Yuck! I feel queazy";

    {
        name = "raw egg";
        image = ItemSpriteSheet.EGG;
    }

    @Override
    public String info() {
        return "A raw egg. I think it might be rotten. ";
    }

    @Override
    public void eat( Hero hero ) {
        super.eat( hero );
        Buff.prolong( hero, Poison.class, 10 );
    }

    @Override
    public float getEnergy() {
        return Food.VERY_LITTLE;
    }

    @Override
    public String getMessage() {
        return MESSAGE;
    }

}

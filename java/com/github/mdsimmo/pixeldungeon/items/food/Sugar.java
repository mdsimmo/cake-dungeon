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
import com.github.mdsimmo.pixeldungeon.actors.buffs.Speed;
import com.github.mdsimmo.pixeldungeon.actors.hero.Hero;
import com.github.mdsimmo.pixeldungeon.sprites.ItemSpriteSheet;

public class Sugar extends Food {

    public static final String MESSAGE = "Your teeth are tingling";

    public static class Hyperactive extends Speed {
        @Override
        public float factor() {
            return cooldown() / 5 + 1;
        }
    }

    {
        name = "sugar";
        image = ItemSpriteSheet.SUGAR;
    }

    @Override
    public void eat( Hero hero ) {
        super.eat( hero );
        Buff.prolong( hero, Hyperactive.class, Speed.DURATION );
    }

    @Override
    public String info() {
        return "I hear sugar makes you hyperactive";
    }

    @Override
    public float getEnergy() {
        return Food.VERY_LITTLE;
    }

    @Override
    public String getMessage() {
        return Sugar.MESSAGE;
    }
}

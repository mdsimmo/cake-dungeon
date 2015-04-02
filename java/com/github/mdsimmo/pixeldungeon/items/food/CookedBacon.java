package com.github.mdsimmo.pixeldungeon.items.food;

import com.github.mdsimmo.pixeldungeon.sprites.ItemSpriteSheet;

public class CookedBacon extends Food {

    {
        name = "cooked bacon";
        image = ItemSpriteSheet.COOKED_BACON;
    }


    @Override
    public String info() {
        return "Crispy bacon! Useful for so many things, like bacon ice-cream cones.";
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

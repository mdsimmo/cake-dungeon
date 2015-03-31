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

import android.util.Log;

import com.github.mdsimmo.pixeldungeon.items.Heap;
import com.github.mdsimmo.pixeldungeon.items.Item;

public class Recipes {

    private static class Recipe {

        private final Item[] ingredients;
        private final Class<? extends Item> result;
        private boolean baked = false;
        private boolean frozen = false;

        public Recipe ( Class<? extends Item> result, Item ... items ) {
            this.ingredients = items;
            this.result = result;
        }

        public Recipe baked() {
            this.baked = true;
            return this;
        }

        public Recipe frozen() {
            this.frozen = true;
            return this;
        }

        public boolean cook( Heap heap, boolean burning, boolean frozen ) {
            if ( ( this.baked && !burning ) || ( this.frozen && !frozen ) )
                return false;
            Item[] heapItems = heap.items.toArray( new Item[heap.items.size()]);

            // check that the item matches
            for ( Item item : ingredients ) {
                boolean matchFound = false;
                for ( Item match : heapItems ) {
                    if ( match.getClass() == item.getClass() && match.quantity() >= item.quantity() ) {
                        matchFound = true;
                        break;
                    }
                }
                if ( !matchFound )
                    return false;
            }

            // cook the item
            for ( Item item : ingredients ) {
                for ( Item match : heapItems ) {
                    if ( match.getClass() == item.getClass() && match.quantity() >= item.quantity() ) {
                        match.quantity( match.quantity() - item.quantity() );
                        if ( match.quantity() <= 0 )
                            heap.items.remove( match );
                        break;
                    }
                }
            }

            makePrize( heap );

            return true;
        }

        public void makePrize( Heap heap ) {
            try {
                heap.drop( result.newInstance() );
            } catch ( Exception e ) {
                Log.w( "Recipes", "Unable to make prize " + result );
            }

        }

    }

    private static Recipe[] recipes = {
            new Recipe( ChargrilledMeat.class, new MysteryMeat() ).baked(),
            new Recipe( FrozenCarpaccio.class, new MysteryMeat() ).frozen(),
            new Recipe( Cake.class, new MysteryMeat(), new FrozenCarpaccio() ),
    };

    public static boolean cook( Heap heap ) {
        for ( Recipe recipe : recipes ) {
            if (recipe.cook( heap, true, false ))
                return true;
        }
        return false;
    }

    public static boolean freeze( Heap heap ) {
        for ( Recipe recipe : recipes ) {
            if (recipe.cook( heap, false, true ))
                return true;
        }
        return false;
    }

    public static boolean combine( Heap heap ) {
        for ( Recipe recipe : recipes ) {
            if (recipe.cook( heap, false, false ))
                return true;
        }
        return false;

    }

}

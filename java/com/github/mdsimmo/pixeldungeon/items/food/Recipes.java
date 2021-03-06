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
import com.github.mdsimmo.pixeldungeon.levels.Level;

public class Recipes {

    private static class Recipe {

        private final Item[] ingredients;
        private final Class<? extends Item> result;
        private Method method = null;
        private boolean damp = false;

        public Recipe( Class<? extends Item> result, Item... items ) {
            this.ingredients = items;
            this.result = result;
        }

        public Recipe set( Method method ) {
            this.method = method;
            return this;
        }

        public Recipe damp() {
            this.damp = true;
            return this;
        }

        public boolean correctEnvironment( Heap heap, Method method ) {
            return (this.method == null || this.method == method)
                    && ( this.damp == false || Level.water[heap.pos]);
        }

        public boolean make( Heap heap, Method method ) {
            if ( !correctEnvironment( heap, method ) )
                return false;

            Item[] heapItems = heap.items.toArray( new Item[heap.items.size()] );

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
            // basic cooking
            new Recipe( ChargrilledMeat.class, new MysteryMeat() ).set( Method.BAKED ),
            new Recipe( CookedBacon.class, new Bacon() ).set( Method.BAKED ),
            new Recipe( GrilledLambChop.class, new LambChop() ).set( Method.BAKED ),
            new Recipe( HardBoiledEgg.class, new RawEgg() ) {
                @Override
                public void makePrize( Heap heap ) {

                    Heap.evaporateFX( heap.pos );
                    super.makePrize( heap );
                }
            }.set( Method.BAKED ).damp(),
            new Recipe( FriedEgg.class, new RawEgg() ).set( Method.BAKED ),
            new Recipe( BakedPotato.class, new RawPotato() ).set( Method.BAKED ),

            // not so obvious
            new Recipe( FrozenCarpaccio.class, new MysteryMeat() ).set( Method.FROZEN ),
            new Recipe( ScrambledEgg.class, new RawEgg() ).set( Method.EXPLODED ),

            // Full recipes
            new Recipe( Cake.class, new Sugar().quantity( 3 ),
                    new Flour().quantity( 2 ), new RawEgg() ).set( Method.BAKED ).damp(),
            new Recipe( Pasty.class, new Flour(), new ChargrilledMeat(), new BakedPotato(),
                    new RawEgg() ).set( Method.BAKED ),

    };

    public enum Method {
        BAKED, FROZEN, EXPLODED;
    }

    public static void make( Heap heap, Method method ) {
        for ( Recipe recipe : recipes ) {
            if ( recipe.make( heap ,method ) )
                return;
        }
    }

}

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
package com.github.mdsimmo.pixeldungeon.ui;

import com.github.mdsimmo.gltextures.TextureCache;
import com.github.mdsimmo.noosa.Image;
import com.github.mdsimmo.noosa.ui.Component;
import com.github.mdsimmo.pixeldungeon.actors.Char;
import com.github.mdsimmo.pixeldungeon.sprites.CharSprite;

public class HealthIndicator extends Component {

	private static final float HEIGHT	= 2;
	
	public static HealthIndicator instance;
	
	private Char target;
	
	private Image bg;
	private Image level;
	
	public HealthIndicator() {
		super();
		
		instance = this;
	}
	
	@Override
	protected void createChildren() {
		bg = new Image( TextureCache.createSolid( 0xFFcc0000 ) );
		bg.scale.y = HEIGHT;
		add( bg );
		
		level = new Image( TextureCache.createSolid( 0xFF00cc00 ) );
		level.scale.y = HEIGHT;
		add( level );
	}
	
	@Override
	public void update() {
		super.update();
		
		if (target != null && target.isAlive() && target.sprite.visible) {
			CharSprite sprite = target.sprite;
			bg.scale.x = sprite.width;
			level.scale.x = sprite.width * target.HP / target.HT;
			bg.x = level.x = sprite.x;
			bg.y = level.y = sprite.y - HEIGHT - 1;
			
			visible = true;
		} else {
			visible = false;
		}
	}
	
	public void target( Char ch ) {
		if (ch != null && ch.isAlive()) {
			target = ch;
		} else {
			target = null;
		}
	}
	
	public Char target() {
		return target;
	}
}

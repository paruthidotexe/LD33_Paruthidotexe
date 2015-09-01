package com.znop.paruthidotexe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.Array;
import com.znop.paruthidotexe.entities.Player;

import java.util.Iterator;

/**
 * Created by user on 30-08-2015.
 */
public class InGame implements Screen {
    TiledMap tiledMap;
    OrthogonalTiledMapRenderer tiledMapRenderer;
    OrthographicCamera cam;
    Player player;

    int[] layer_Bg_01 = new int[]{0};
    int[] layer_LevelMap = new int[]{1};
    int[] layer_Fg_01 = new int[]{2};

    @Override
    public void show() {
        tiledMap = new TmxMapLoader().load("maps/level_0.tmx");

        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        // camera
        cam = new OrthographicCamera();
        cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.update();

        // player
        player = new Player(new Sprite(new Texture("Monster_01.png")),
                (TiledMapTileLayer)tiledMap.getLayers().get("LevelMap"));
        player.setPosition(10, Gdx.graphics.getHeight() * 3);

        //input
        Gdx.input.setInputProcessor(player);

        // anim tiles
        Array<StaticTiledMapTile> frameTiles = new Array<StaticTiledMapTile>(4);
        Iterator<TiledMapTile> tiles = tiledMap.getTileSets().getTileSet("spritesheet_items").iterator();
        while(tiles.hasNext())
        {
            TiledMapTile curTile = tiles.next();
            if(curTile.getProperties().containsKey("animation") &&
                    curTile.getProperties().get("animation", String.class).equals("diamond"))
            {
                frameTiles.add((StaticTiledMapTile)curTile);
            }
        }
        AnimatedTiledMapTile animTile = new AnimatedTiledMapTile(1/4f, frameTiles);
        
        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get("Fg_01");
        for(int x = 0; x < layer.getWidth(); x++)
        {
            for(int y = 0; y < layer.getHeight(); y++)
            {
                TiledMapTileLayer.Cell curCell = layer.getCell(x, y);
                if(curCell!= null) {
                    if (curCell.getTile().getProperties().containsKey("animation") &&
                            curCell.getTile().getProperties().get("animation", String.class).equals("diamond")) {
                        curCell.setTile(animTile);
                    }
                }
            }
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        cam.position.set(player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2, 0);
        cam.update();
        tiledMapRenderer.setView(cam);

        tiledMapRenderer.render(layer_Bg_01);
        tiledMapRenderer.render(layer_LevelMap);

        tiledMapRenderer.getBatch().begin();
        player.draw(tiledMapRenderer.getBatch());
        tiledMapRenderer.getBatch().end();

        tiledMapRenderer.render(layer_Fg_01);
    }

    @Override
    public void resize(int width, int height) {
        cam.viewportWidth = width;
        cam.viewportHeight = height;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        tiledMap.dispose();
        tiledMapRenderer.dispose();
        player.getTexture().dispose();
    }
}

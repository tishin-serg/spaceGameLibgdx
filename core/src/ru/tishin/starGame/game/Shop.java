package ru.tishin.starGame.game;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import ru.tishin.starGame.screen.utils.Assets;

public class Shop extends Group {
    private final Shop thisShop = this;
    private Hero hero;
    private BitmapFont font24;
    private boolean isVisible;
    private TextButton btnClose;
    private TextButton btnHpMax;
    private TextButton btnHp;
    private TextButton btnWeapon;

    public Shop(Hero hero) {
        this.hero = hero;
        this.font24 = Assets.getInstance().getAssetManager().get("fonts/font24.ttf");
        this.isVisible = false;

        Pixmap pixmap = new Pixmap(400, 400, Pixmap.Format.RGB888);
        pixmap.setColor(0, 0, 0.5f, 1);
        pixmap.fill();

        Image image = new Image(new Texture(pixmap));
        this.addActor(image);

        Skin skin = new Skin();
        skin.addRegions(Assets.getInstance().getAtlas());

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("shortButton");
        textButtonStyle.font = font24;
        skin.add("simpleSkin", textButtonStyle);

        initBtnClose(textButtonStyle);
        initBtnHpMax(textButtonStyle);
        initBtnHp(textButtonStyle);
        initBtnWeapon(textButtonStyle);

        this.setPosition(20, 20);
        this.setVisible(false);
        skin.dispose();
    }

    public void changeVisible() {
        this.setVisible(isVisible = !isVisible);
    }

    private void initBtnClose(TextButton.TextButtonStyle textButtonStyle) {
        btnClose = new TextButton("X", textButtonStyle);
        btnClose.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                thisShop.setVisible(false);
            }
        });

        btnClose.setTransform(true);
        btnClose.setScale(0.5f);
        btnClose.setPosition(355, 355);
        this.addActor(btnClose);
    }

    private void initBtnHpMax(TextButton.TextButtonStyle textButtonStyle) {
        btnHpMax = new TextButton("hpMax+", textButtonStyle);
        btnHpMax.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (hero.isMoneyEnough(Hero.Skill.HP_MAX.cost)) {
                    if (hero.upgradeFromStore(Hero.Skill.HP_MAX)) {
                        hero.decreaseMoney(Hero.Skill.HP_MAX.cost);
                    }
                }
            }
        });

        btnHpMax.setPosition(20, 300);
        this.addActor(btnHpMax);
    }

    private void initBtnHp(TextButton.TextButtonStyle textButtonStyle) {
        btnHp = new TextButton("hp+", textButtonStyle);
        btnHp.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (hero.isMoneyEnough(Hero.Skill.HP.cost)) {
                    if (hero.upgradeFromStore(Hero.Skill.HP)) {
                        hero.decreaseMoney(Hero.Skill.HP.cost);
                    }
                }
            }
        });

        btnHp.setPosition(20, 200);
        this.addActor(btnHp);
    }

    private void initBtnWeapon(TextButton.TextButtonStyle textButtonStyle) {
        btnWeapon = new TextButton("weapon+", textButtonStyle);
        btnWeapon.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (hero.isMoneyEnough(Hero.Skill.WEAPON.cost)) {
                    if (hero.upgradeFromStore(Hero.Skill.WEAPON)) {
                        hero.decreaseMoney(Hero.Skill.WEAPON.cost);
                    }
                }
            }
        });

        btnWeapon.setPosition(20, 100);
        this.addActor(btnWeapon);
    }
}

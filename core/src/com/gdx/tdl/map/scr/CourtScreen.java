package com.gdx.tdl.map.scr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.gdx.tdl.map.ags.Ball;
import com.gdx.tdl.map.ags.EmptyAgent;
import com.gdx.tdl.util.map.OptionButton;
import com.gdx.tdl.util.map.OptionsTable;
import com.gdx.tdl.map.ags.PlayerD;
import com.gdx.tdl.map.ags.PlayerO;
import com.gdx.tdl.map.tct.Tactic;
import com.gdx.tdl.util.AssetLoader;
import com.gdx.tdl.util.map.AbstractScreen;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CourtScreen extends AbstractScreen implements GestureDetector.GestureListener {
    private static final int MENU   = 0;
    private static final int FRAME  = 1;
    private static final int RUN    = 2;
    private static final int PASS   = 3;
    private static final int SCREEN = 4;
    private static final int HELP   = 5;

    private static final int PLAY   = 6;
    private static final int RESET  = 7;

    private static final int MANMAN = 8;
    private static final int ZONE   = 9;

    private static final int SVFILE = 10;
    private static final int SVPDF  = 11;
    private static final int SVVID  = 12;
    private static final int NOTES  = 13;

    private ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);

    private float[] elColor = new float[] { 0, 120/255f, 200/255f };

    private EmptyAgent basket;
    private PlayerO[] offense;
    private PlayerD[] defense;
    private Ball ball;

    private Tactic tactic;
    private boolean permissionToPlay, stillPlayingMove;
    private int flag, firstPlayerWithBall;

    private OptionButton[] offensiveOptions, defensiveOptions, tacticCreationOptions, saveOptions;

    private MenuScreen menu;
    private HelpScreen help;

    private int trueCounter = 0;

    private int playerWithBall;

    private Body bodyHit;
    private int bodyHitId, option, btn;
    private boolean cancelSelect, isSomeoneSelected;
    private boolean menuSelected, helpSelected;

    public CourtScreen() {
        super();

        // tatica a ser definida
        tactic = new Tactic();
        permissionToPlay = stillPlayingMove = false;

        // tabela com as opcoes ofensivas para os jogadores
        optionsTable.offensiveOptionsDraw();
        offensiveOptions = optionsTable.getOffensiveOptions();
        optionsTable.defensiveOptionsDraw();
        defensiveOptions = optionsTable.getDefensiveOptions();
        optionsTable.tacticCreationOptionsDraw();
        tacticCreationOptions = optionsTable.getTacticCreationOptions();
        optionsTable.saveOptionsDraw();
        saveOptions = optionsTable.getSaveOptions();

        // ecras secundarios
        menu = new MenuScreen(world, optionsTable);
        help = new HelpScreen(world, optionsTable);

        // inicializacao das variaveis de ajuda a percecao da intencao do user
        bodyHit = null;
        bodyHitId = option = btn = -1;
        cancelSelect = isSomeoneSelected = false;

        // variaveis auxiliares
        flag = 5;
        playerWithBall = 1;
        menuSelected = helpSelected = false;

        //
        float playerBoundingRadius = Gdx.graphics.getWidth() / 48f;
        float ballBoundingRadius = Gdx.graphics.getWidth() / 116f;
        float basketBoundingRadius = 0.1f;


        // inicializacao do cesto
        Vector2 posBasket = new Vector2(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()*6/7f);
        basket = new EmptyAgent(world, posBasket, basketBoundingRadius);

        // inicializacao dos jogadores
        offense = new PlayerO[5];
        defense = new PlayerD[5];

        float i = 2;
        for (int n = 1; n <= 5; n++) {
            Vector2 posO = new Vector2(Gdx.graphics.getWidth()*i/10f, Gdx.graphics.getHeight()/10f);
            offense[n-1] = new PlayerO(world, posO, playerBoundingRadius, n == 1, n);
            Vector2 posD = new Vector2(Gdx.graphics.getWidth()*i/10f,Gdx.graphics.getHeight()*9/10f);
            defense[n-1] = new PlayerD(world, posD, playerBoundingRadius, n);
            i += 1.75f;
        }

        // inicializacao da bola e respetivo comportamento
        Vector2 posBall = offense[0].getBody().getPosition().add(new Vector2(playerBoundingRadius, playerBoundingRadius));
        ball = new Ball(world, posBall, ballBoundingRadius, offense[0].getTarget());
        ball.setTarget(new EmptyAgent(world, offense[0].getTarget().getBody().getPosition(), 0.01f));
        ball.setBasketTarget(basket);
        Arrive<Vector2> ballBehaviour = new Arrive<>(ball, ball.getTarget())
                .setTimeToTarget(0.01f)
                .setArrivalTolerance(0.1f)
                .setDecelerationRadius(playerBoundingRadius * 2)
                .setEnabled(true);
        ball.setBehaviour(ballBehaviour);

        // atribuicao dos comportamentos dos atacantes
        for (PlayerO playerO : offense) {
            Arrive<Vector2> arriveBO = new Arrive<>(playerO, playerO.getTarget())
                    .setTimeToTarget(0.01f)
                    .setArrivalTolerance(0.01f)
                    .setDecelerationRadius(playerBoundingRadius * 4)
                    .setEnabled(true);

            /*Box2dRadiusProximity proximity = new Box2dRadiusProximity(playerO, world, playerO.getBoundingRadius()*1.5f);
            CollisionAvoidance<Vector2> collisionAvoid = new CollisionAvoidance<>(playerO, proximity);

            LookWhereYouAreGoing<Vector2> lwyag = new LookWhereYouAreGoing<>(playerO)
                .setTimeToTarget(0.01f)
                .setAlignTolerance(50f)
                .setDecelerationRadius(playerO.getBoundingRadius())
                .setTarget(basket);*/

            BlendedSteering<Vector2> behaviours = new BlendedSteering<>(playerO)
                    .add(arriveBO, 1f);
            //.add(collisionAvoid, 1f)
            //.add(lwyag, 1f);

            playerO.setBehaviour(behaviours);
        }

        // atribuicao dos comportamentos dos defensores
        for (PlayerD playerD : defense) {
            playerD.setMainTarget(new EmptyAgent(world, playerD.getBody().getPosition(), 0.01f));
            playerD.setPlayerTarget(offense[playerD.getNum()-1]);
            playerD.setBasketTarget(basket);
            playerD.setPlayerWithBall(offense[playerWithBall-1]);

            Arrive<Vector2> arriveBD = new Arrive<>(playerD, playerD.getMainTarget())
                    .setTimeToTarget(0.01f)
                    .setArrivalTolerance(0.01f)
                    .setDecelerationRadius(playerBoundingRadius * 4)
                    .setEnabled(true);

            /*Box2dRadiusProximity proximity = new Box2dRadiusProximity(playerD, world, playerD.getBoundingRadius()*1.5f);
            CollisionAvoidance<Vector2> collisionAvoid = new CollisionAvoidance<>(playerD, proximity);

            LookWhereYouAreGoing<Vector2> lwyag = new LookWhereYouAreGoing<>(playerD)
                    .setTimeToTarget(0.01f)
                    .setAlignTolerance(10f)
                    .setDecelerationRadius(playerD.getBoundingRadius())
                    .setTarget(offense[playerD.getNum()-1]);*/

            BlendedSteering<Vector2> behaviours = new BlendedSteering<>(playerD)
                    .add(arriveBD, 1f);
            //.add(collisionAvoid, 1f)
            //.add(lwyag, 1f);

            playerD.setBehaviour(behaviours);
        }

        Gdx.input.setInputProcessor(new GestureDetector(this));
    }

    @Override
    public void buildScreen(float delta) {
        // cor de fundo
        Gdx.gl.glClearColor(elColor[0], elColor[1], elColor[2], 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (permissionToPlay)
            playTactic();

        if (menuSelected) {
            menu.menuDraw();
        } else if (helpSelected) {
            help.helpDraw();
        } else {
            // background
            AssetLoader.batch.begin();
            AssetLoader.batch.draw(AssetLoader.I_court, Gdx.graphics.getWidth()/10f, 0, Gdx.graphics.getWidth()*9/10f, Gdx.graphics.getHeight());
            AssetLoader.batch.end();

            // updates agents on render
            basket.update(delta);
            for (PlayerO playerO : offense) playerO.update(delta);
            for (PlayerD playerD : defense) playerD.update(delta);
            ball.update(delta);

            if (optionsTable.getCurrentOption() == OptionsTable.OFFE) {
                optionsTable.update(offensiveOptions);
            } else if (optionsTable.getCurrentOption() == OptionsTable.DEFE) {
                optionsTable.update(defensiveOptions);
            } else if (optionsTable.getCurrentOption() == OptionsTable.TACT) {
                optionsTable.update(tacticCreationOptions);
            } else if (optionsTable.getCurrentOption() == OptionsTable.SAVE) {
                optionsTable.update(saveOptions);
            }
        }
    }



    /**
     * Metodos auxiliares
     */

    // verifica se o body foi tocado na tal posicao
    private boolean touchedBody(Body body, Vector2 pos) {
        for (Fixture fixture : body.getFixtureList())
            if (fixture.testPoint(pos.x, pos.y))
                return true;
        return false;
    }

    // atualiza o botao premido
    private void setBtn(OptionButton[] options, Vector2 posHit) {
        for (int b = 0; b < options.length; b++) {
            if (touchedBody(options[b].getBody(), posHit)) {
                btn = b;
                return;
            }
        }
    }

    // TODO verifica que botao foi premido
    private int buttonHit() {
        // botoes disponiveis dependem do ecra atual
        if (menuSelected) {
            if (btn == 0) return MENU;
            if (btn == 1) return HELP;
        }

        if (!helpSelected) {
            if (btn == 0) return MENU;

            int opt = optionsTable.getCurrentOption();

            if (opt == OptionsTable.OFFE) {
                if (btn == 1) return FRAME;
                if (btn == 2) return RUN;
                if (btn == 3) return PASS;
                if (btn == 4) return SCREEN;
                if (btn == 5) return HELP;
            } else if (opt == OptionsTable.DEFE) {
                if (btn == 1) return MANMAN;
                if (btn == 2) return ZONE;
                if (btn == 3) return HELP;
            } else if (opt == OptionsTable.TACT) {
                if (btn == 1) return PLAY;
                if (btn == 2) return FRAME;
                if (btn == 3) return RESET;
                if (btn == 4) return HELP;
            } else if (opt == OptionsTable.SAVE) {
                if (btn == 1) return SVFILE;
                if (btn == 2) return SVPDF;
                if (btn == 3) return SVVID;
                if (btn == 4) return NOTES;
                if (btn == 5) return HELP;
            }

        } else if (btn == 0) return HELP;

        return -1;
    }

    // selecciona um botao
    private void tagButton() {
        int curr = optionsTable.getCurrentOption();

        if (curr == OptionsTable.OFFE) {
            offensiveOptions[btn].setIsSelected(true);
            for (OptionButton opt : offensiveOptions)
                if (opt != offensiveOptions[btn])
                    opt.setIsSelected(false);
        } else if (curr == OptionsTable.DEFE) {
            defensiveOptions[btn].setIsSelected(true);
            for (OptionButton opt : defensiveOptions)
                if (opt != defensiveOptions[btn])
                    opt.setIsSelected(false);
        } else if (curr == OptionsTable.TACT) {
            tacticCreationOptions[btn].setIsSelected(true);
            for (OptionButton opt : tacticCreationOptions)
                if (opt != tacticCreationOptions[btn])
                    opt.setIsSelected(false);
        }
    }

    // selecciona um player
    private void tagPlayer() {
        if ((option == PASS || option == SCREEN) && !offense[bodyHitId].hasBall())
            return;
        else {
            offense[bodyHitId].setTagged(true);
            isSomeoneSelected = true;
            for (PlayerO playerO : offense)
                if (playerO != offense[bodyHitId])
                    playerO.setTagged(false);
        }
    }

    // retorna o player que estiver seleccionado
    private int whoIsTagged() {
        for (int o = 0; o < offense.length; o++)
            if (offense[o].isTagged())
                return o;
        return -1;
    }

    // retira o check de cada player apos guardada cada frame
    private void uncheckPlayers(PlayerO[] players) {
        for (PlayerO player : players)
            player.setHasMoved(false);
    }

    private void reset() {
        bodyHit   = null;
        bodyHitId = -1;
        btn       = -1;
        option    = -1;
        cancelSelect = false;
        isSomeoneSelected = false;

        for (OptionButton opt : offensiveOptions)
            opt.setIsSelected(false);
    }



    /**
     * Metodos principais
     */

    // movimento de corrida do atacante
    private void playerRun(Vector2 posHit) {
        offense[bodyHitId].setTargetPosition(posHit);
        offense[bodyHitId].setTagged(false);
        if (!stillPlayingMove) {
            offense[bodyHitId].setLastMove(RUN);
            offense[bodyHitId].setHasMoved(true);
        }
    }

    // movimento de drible do atacante
    private void playerDribble(Vector2 posHit) {
        // TODO diferenca esta na direcao em que esta virado (pode nao dar, implementa depois)
        // aplicar JUMP na bola
    }

    // bola passa de A para B
    private void passTheBall() {
        int w = -1;

        // caso esteja em modo de reproducao, nao necessita estar tagged
        if (!stillPlayingMove) {
            w = whoIsTagged();
        } else {
            for (int p = 0; p < offense.length; p++) {
                if (offense[p].hasBall()) {
                    w = p;
                    break;
                }
            }
        }

        if (offense[w].hasBall()) {
            offense[w].setTagged(false);
            offense[w].setHasBall(false);
            offense[w].setLastMove(PASS);
            offense[w].setReceiver(bodyHitId);
            offense[bodyHitId].setHasBall(true);
            if (!stillPlayingMove) {
                offense[w].setHasMoved(true);
                offense[bodyHitId].setHasMoved(true);
            }
            ball.setPlayerToFollow(offense[bodyHitId].getTarget());
            playerWithBall = bodyHitId;

            for (PlayerD player : defense)
                player.setPlayerWithBall(offense[bodyHitId]);
        }
    }

    // aplica um bloqueio ao defensor do colega
    private void doAScreen() {
        // TODO caso se consiga implementar o collision avoid
    }

    // aplica a acao correspondente ao atacante
    private void playerAction(Vector2 posHit) {
        switch (option) {
            case RUN:
                if (bodyHit == null) {
                    playerRun(posHit);
                    reset();
                }
                break;
            case PASS:
                if (bodyHit != null) {
                    passTheBall();
                    reset();
                }
                break;
            case SCREEN:
                doAScreen();
                reset();
                break;
        }
    }

    // adicao de uma nova frame a tatica atual
    private void addNewFrame() {
        // alteracoes necessarias no ecra
        menuSelected = false;
        uncheckPlayers(offense);

        // definidas as posicoes iniciais
        if (tactic.getNFrames() == -1) {
            for (int d = 0; d < offense.length; d++) {
                Vector2 posInit = offense[d].getBody().getPosition().cpy();
                tactic.addInitialPos(d, posInit);
                if (offense[d].hasBall()) firstPlayerWithBall = d;
            }
            tactic.setToBegin();
        }

        // adicionada nova frame a lista
        else {
            int size = tactic.getSize();
            for (int d = size; d < offense.length + size; d++) {
                Integer[] moves = new Integer[] { offense[d % 5].getLastMove(), offense[d % 5].getReceiver() };
                tactic.addToMovements(d, moves, offense[d % 5].getTarget().getBody().getPosition().cpy());
                offense[d % 5].setLastMove(-1);
                offense[d % 5].setReceiver(-1);
            }
            tactic.setNFrames(tactic.getNFrames() + 1);
        }

        // da permissao aos defesas para seguir os atacantes
        /*if (!defense[0].getPermissionToFollow())
            for (PlayerD player : defense)
                player.setPermissionToFollow(true);*/
    }

    // aplica o movimento durante a reproducao da jogada
    private void applyMove(int f, int move, int receiver) {
        option = move;
        bodyHitId = f % 5;

        if (option == PASS) {
            bodyHitId = receiver;
            if (!offense[bodyHitId].hasBall()) {
                bodyHit = offense[bodyHitId].getBody();
            }
        } // else if (option == SCREEN)

        playerAction(tactic.getEntryValue(f));

        waitTime(1);
    }

    // reproduz a tatica atual
    private void playTactic() {
        if (tactic.initialPos != null && tactic.getSize() > 0 && tactic.getNFrames() > 0) {
            // percorre lista de frames (de cinco em cinco) e aplica movimentos
            exec.schedule(() -> {
                stillPlayingMove = true;

                for (int f = flag - 5; f < flag; f++) {
                    System.out.println("Fez");
                    applyMove(f, tactic.getEntryKey(f)[0], tactic.getEntryKey(f)[1]);
                }

                waitTime(5000);
                flag += 5;

            }, 2, TimeUnit.SECONDS);

            tactic.setNFrames(tactic.getNFrames() - 1);

            // verifica a situacao da lista
            if (tactic.getNFrames() <= 0) {
                permissionToPlay = false;
                stillPlayingMove = false;
            }
        }
        // TODO pop up no ecra com a info de que nao existe nada para reproduzir
    }

    // limpa a tatica atual
    private void resetTactic() {
        tactic.cleanMovements();
        tactic.cleanInitPos();

        for (PlayerO playerO : offense) {
            playerO.setLastMove(-1);
            playerO.setReceiver(-1);
            playerO.setTagged(false);
            playerO.setHasBall(false);
            playerO.setHasMoved(false);
            playerO.setTargetPosition(playerO.getInitialPos());
            playerO.setAtPosition(playerO.getTarget().getBody().getPosition());
        }

        for (PlayerD playerD : defense) {
            playerD.setPermissionToFollow(false);
            playerD.setAtPosition(playerD.getInitialPos());
            playerD.setPlayerWithBall(offense[firstPlayerWithBall]);
        }

        reset();
        uncheckPlayers(offense);
        if (menuSelected) menuSelected = false;

        offense[firstPlayerWithBall].setHasBall(true);
        ball.setPlayerToFollow(offense[firstPlayerWithBall].getTarget());
        ball.setAtPosition(ball.getTarget().getPosition());
    }

    // verifica no que tocou
    private void whatDidITouch(Vector2 posHit) {
        bodyHit = null;

        int curr = optionsTable.getCurrentOption();

        // verifica se carregou num botao das opcoes do ecra principal, do menu ou do help
        if (menuSelected) setBtn(menu.getMenuOptions(), posHit);
        else if (helpSelected) setBtn(help.getHelpOptions(), posHit);
        else if (curr == OptionsTable.OFFE) setBtn(offensiveOptions, posHit);
        else if (curr == OptionsTable.DEFE) setBtn(defensiveOptions, posHit);
        else if (curr == OptionsTable.TACT) setBtn(tacticCreationOptions, posHit);
        else if (curr == OptionsTable.SAVE) setBtn(saveOptions, posHit);

        // verifica se e um body indiferente ao toque (bola, cesto ou defesa)
        if (touchedBody(ball.getBody(), posHit)) return;
        if (touchedBody(basket.getBody(), posHit)) return;
        for (PlayerD pd : defense)
            if (touchedBody(pd.getBody(), posHit)) return;

        // verifica se tocou num atacante
        if (option != -1) {
            for (int i = 0; i < offense.length; i++) {
                if (touchedBody(offense[i].getBody(), posHit)) {
                    if (bodyHitId == i)
                        cancelSelect = true;
                    else {
                        bodyHit = offense[i].getBody();
                        if (!isSomeoneSelected || option == PASS || option == SCREEN)
                            bodyHitId = i;
                    }
                    return;
                }
            }
        }
    }

    private void oneTap(float x, float y) {
        final Vector2 posHit = new Vector2(x, Gdx.graphics.getHeight() - y);

        // primeiro ve no que tocou
        whatDidITouch(posHit);

        // verifica se foi um botao novo
        if (option != btn) {
            option = buttonHit();
            tagButton(); // TODO CHECK

            // reproducao da tatica
            if (option == PLAY && tactic.getSize() > 0) {
                tactic.setNFrames(tactic.getSize() / 5);
                uncheckPlayers(offense);
                menuSelected = false;
                flag = 5;

                // coloca players e bola na posicao inicial
                for (int p = 0; p < tactic.initialPos.length; p++) {
                    Vector2 init = tactic.initialPos[p];
                    offense[p].setTargetPosition(init);
                    offense[p].setAtPosition(init);
                }

                // posse de bola no inicio da jogada
                offense[firstPlayerWithBall].setHasBall(true);
                ball.setPlayerToFollow(offense[firstPlayerWithBall].getTarget());
                ball.setAtPosition(offense[firstPlayerWithBall].getTarget().getPosition());
                for (PlayerO player : offense)
                    if (!player.equals(offense[firstPlayerWithBall]))
                        player.setHasBall(false);
                for (PlayerD player : defense)
                    player.setPlayerWithBall(offense[firstPlayerWithBall]);

                // da permissao para reproduzir a jogada
                permissionToPlay = true;
            }

            // adicao de uma nova frame
            if (option == FRAME) addNewFrame();

            // limpeza da tatica
            if (option == RESET) resetTactic();

            // caso o botao seja para mudar de ecra
            if (option == MENU) {
                menuSelected = !menuSelected;
                helpSelected = false;
                reset();
            } else if (option == HELP) {
                helpSelected = !helpSelected;
                menuSelected = false;
                reset();
            }

            return;
        }

        // verifica se tocou num atacante
        if (bodyHitId != -1) {
            if (!isSomeoneSelected) {
                tagPlayer();
                return;
            }

            if (cancelSelect) {
                cancelSelect = false;
                isSomeoneSelected = false;
                offense[bodyHitId].setTagged(false);
                bodyHitId = -1;
                return;
            }
        } else return;

        playerAction(posHit);
    }


    @Override
    public boolean tap(float x, float y, int count, int button) {
        trueCounter = count;
        if (trueCounter > 1) {
            int opt = optionsTable.getCurrentOption();

            if (opt == OptionsTable.OFFE) {
                optionsTable.setCurrentOption(OptionsTable.DEFE);
                elColor[0] = 200/255f;
                elColor[1] = 20/255f;
                elColor[2] = 0;
            } else if (opt == OptionsTable.DEFE) {
                optionsTable.setCurrentOption(OptionsTable.TACT);
                elColor[0] = 200/255f;
                elColor[1] = 80/255f;
                elColor[2] = 0;
            } else if (opt == OptionsTable.TACT) {
                optionsTable.setCurrentOption(OptionsTable.SAVE);
                elColor[0] = 50/255f;
                elColor[1] = 150/255f;
                elColor[2] = 50/255f;
            } else if (opt == OptionsTable.SAVE) {
                optionsTable.setCurrentOption(OptionsTable.OFFE);
                elColor[0] = 0;
                elColor[1] = 120/255f;
                elColor[2] = 200/255f;
            }

            trueCounter = 0;
        } else {
            exec.schedule(() -> {
                if (trueCounter == 1) {
                    oneTap(x, y);
                    trueCounter = 0;
                }
            }, 200, TimeUnit.MILLISECONDS);
        }

        return false;
    }


    /**
     * Outras funcoes
     */

    private void waitTime(int time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override // TODO Maybe usar para definir posicoes iniciais dos jogadores?
    public boolean longPress(float x, float y) { return false; }

    @Override public boolean touchDown(float x, float y, int pointer, int button) { return false; }
    @Override public boolean fling(float velocityX, float velocityY, int button) { return false; }
    @Override public boolean pan(float x, float y, float deltaX, float deltaY) { return false; }
    @Override public boolean panStop(float x, float y, int pointer, int button) { return false; }
    @Override public boolean zoom(float initialDistance, float distance) { return false; }
    @Override public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) { return false; }
    @Override public void pinchStop() { }





    /*/ adiciona uma nova tatica a lista
    @Deprecated
    private void addNewTactic() {
        tactics.add(new TFrame());
        nTactics++;

        // faz reset aos players
        for (PlayerO player : offense)
            player.setPosToInitial();
        uncheckPlayers(offense);

        for (PlayerD player : defense) {
            player.setPermissionToFollow(false);
            player.setPosToInitial();
        }
    }*/
}

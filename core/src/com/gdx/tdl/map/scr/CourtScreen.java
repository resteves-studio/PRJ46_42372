package com.gdx.tdl.map.scr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.graphics.GL20;
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

public class CourtScreen extends AbstractScreen {
    private static final int MENU   = 0;
    private static final int RUN    = 1;
    private static final int DRIBLE = 2;
    private static final int PASS   = 3;
    private static final int SCREEN = 4;
    private static final int HELP   = 5;
    private static final int PLAY   = 6;
    private static final int FRAME  = 7;
    private static final int RESET  = 8;

    ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);

    private EmptyAgent basket;
    private PlayerO[] offense;
    private PlayerD[] defense;
    private Ball ball;

    private Tactic tactic;
    private boolean permissionToPlay, stillPlayingMove;
    private int flag, firstPlayerWithBall;

    private OptionButton[] offensiveOptions;

    private MenuScreen menu;
    private HelpScreen help;

    private int playerWithBall;

    private Body bodyHit;
    private int bodyHitId, option, btn;
    private boolean cancelSelect, isSomeoneSelected;
    private boolean blueColor, menuSelected, helpSelected;

    public CourtScreen() {
        super();

        // tatica a ser definida
        tactic = new Tactic();
        permissionToPlay = stillPlayingMove = false;

        // tabela com as opcoes ofensivas para os jogadores
        optionsTable.offensiveOptionsDraw(world);
        offensiveOptions = optionsTable.getOffensiveOptions();

        // ecras secundarios
        menu = new MenuScreen(world, optionsTable);
        help = new HelpScreen(world, optionsTable);

        // inicializacao das variaveis de ajuda a percecao da intencao do user
        bodyHit = null;
        bodyHitId = option = btn = -1;
        cancelSelect = isSomeoneSelected = false;

        // variaveis auxiliares
        flag = 5;
        blueColor = true;
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

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void buildScreen(float delta) {
        // cor de fundo
        Gdx.gl.glClearColor(blueColor ? 0 : 200/255f, blueColor ? 120/255f : 0, blueColor ? 200/255f : 0, 1f);
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
            } // else if defensive options
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
            if (btn == 2) return PLAY;
            if (btn == 3) return FRAME;
            if (btn == 4) return RESET;
        }

        if (!helpSelected) {
            if (btn == 0) return MENU;
            if (btn == 1) return RUN;
            if (btn == 2) return DRIBLE;
            if (btn == 3) return PASS;
            if (btn == 4) return SCREEN;
            if (btn == 5) return HELP;
        } else if (btn == 0) return HELP;

        return -1;
    }

    // selecciona um botao
    private void tagButton() {
        offensiveOptions[option].setIsSelected(true);
        for (OptionButton opt : offensiveOptions)
            if (opt != offensiveOptions[option])
                opt.setIsSelected(false);
    }

    // selecciona um player
    private boolean tagPlayer() {
        if ((option == PASS || option == SCREEN) && !offense[bodyHitId].hasBall())
            return false;
        else {
            offense[bodyHitId].setTagged(true);
            isSomeoneSelected = true;
            for (PlayerO playerO : offense)
                if (playerO != offense[bodyHitId])
                    playerO.setTagged(false);
        }
        return false;
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
    private boolean playerAction(Vector2 posHit) {
        switch (option) {
            case RUN:
                if (bodyHit == null) {
                    playerRun(posHit);
                    reset();
                }
                break;
            case DRIBLE:
                playerDribble(posHit);
                reset();
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

        return false;
    }

    // adicao de uma nova frame a tatica atual
    private void addNewFrame() {
        // alteracoes necessarias no ecra
        menuSelected = !menuSelected;
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
        menuSelected = !menuSelected;

        offense[firstPlayerWithBall].setHasBall(true);
        ball.setPlayerToFollow(offense[firstPlayerWithBall].getTarget());
        ball.setAtPosition(ball.getTarget().getPosition());
    }

    // verifica no que tocou
    private void whatDidITouch(Vector2 posHit) {
        bodyHit = null;

        // verifica se carregou num botao das opcoes do ecra principal, do menu ou do help
        if (menuSelected)      setBtn(menu.getMenuOptions(), posHit);
        else if (helpSelected) setBtn(help.getHelpOptions(), posHit);
        else if (optionsTable.getCurrentOption() == OptionsTable.OFFE) setBtn(offensiveOptions, posHit);
        /*else if (optionsTable.getCurrentOption() == OptionsTable.DEFE)
            setBtn(defensiveOptions, posHit);*/

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

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        final Vector2 posHit = new Vector2(screenX, Gdx.graphics.getHeight() - screenY);

        // primeiro ve no que tocou
        whatDidITouch(posHit);

        // verifica se foi um botao novo
        if (option != btn) {
            option = buttonHit();
            if (option < 6) tagButton(); // TODO CHECK

            // reproducao da tatica
            if (option == PLAY && tactic.getSize() > 0) {
                tactic.setNFrames(tactic.getSize() / 5);
                menuSelected = !menuSelected;
                uncheckPlayers(offense);
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

            return false;
        }

        // verifica se tocou num atacante
        if (bodyHitId != -1) {
            if (!isSomeoneSelected)
                return tagPlayer();

            if (cancelSelect) {
                cancelSelect = false;
                isSomeoneSelected = false;
                offense[bodyHitId].setTagged(false);
                bodyHitId = -1;
                return false;
            }
        } else return false;

        return playerAction(posHit);
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

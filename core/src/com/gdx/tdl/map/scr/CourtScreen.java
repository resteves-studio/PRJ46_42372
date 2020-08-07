package com.gdx.tdl.map.scr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.steer.behaviors.CollisionAvoidance;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.gdx.tdl.map.ags.Ball;
import com.gdx.tdl.map.ags.EmptyAgent;
import com.gdx.tdl.map.ags.PlayerNumber;
import com.gdx.tdl.map.dlg.DialogIntro;
import com.gdx.tdl.map.dlg.DialogLoadFile;
import com.gdx.tdl.map.dlg.DialogNotes;
import com.gdx.tdl.map.dlg.DialogSaveFile;
import com.gdx.tdl.map.dlg.DialogToast;
import com.gdx.tdl.map.tct.SaveLoad;
import com.gdx.tdl.util.map.Box2dRadiusProximity;
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
    // opcoes de botoes existentes
    private static final int MENU   = 0, FRAME  = 1, RUN = 2, PASS = 3, SCREEN = 4, HELP = 5;
    private static final int PLAY   = 6, RESET  = 7;
    private static final int MANMAN = 8, ZONE   = 9;
    private static final int SVFILE = 10, LDFILE = 11, NOTES = 12;//SVPDF = 11, SVVID = 12;
    private OptionButton[] offensiveOptions, defensiveOptions, tacticCreationOptions, saveOptions;

    // timer
    private ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);

    // cor de fundo
    private float[] elColor = new float[] { 0, 120/255f, 200/255f };
    private String courtColor = "Madeira";

    // agentes
    private PlayerO[] offense = new PlayerO[5];
    private PlayerD[] defense = new PlayerD[5];
    private PlayerNumber[] offenseNs = new PlayerNumber[5];
    private PlayerNumber[] defenseNs = new PlayerNumber[5];
    private EmptyAgent basket;
    private Ball ball;

    // tatica
    private Tactic tactic = new Tactic();
    private boolean permissionToPlay = false;
    private boolean stillPlayingMove = false;
    private int firstPlayerWithBall;
    private int playerWithBall = 1;
    private int flag = 5;

    // dialogs
    private static final int D_NOPLAY = 0;
    private static final int D_FILE = 1, D_UPLD = 2, D_NOTE = 3;
    private static final int D_FAIL = 4, D_SUCCESS = 5;
    private int currentDialog = -1;

    private SaveLoad saveLoad = new SaveLoad(tactic);

    private DialogIntro dialogIntro = new DialogIntro();
    private DialogToast dialogNoPlay = new DialogToast("Nenhuma tatica por reproduzir", saveLoad);
    private DialogSaveFile dialogSaveFile = new DialogSaveFile(saveLoad);
    private DialogLoadFile dialogLoadFile = new DialogLoadFile(saveLoad);
    private DialogNotes dialogNotes = new DialogNotes(saveLoad);
    private DialogToast failDialog = new DialogToast("Nao foi possivel carregar", saveLoad);
    private DialogToast successDialog = new DialogToast("Carregado com sucesso", saveLoad);

    // ecras
    private ScreenHelper screenHelper;

    // gesture detector
    private GestureDetector gestureDetector = new GestureDetector(this);

    // counter do numero de taps
    private int trueCounter = 0;

    // bodys dos agentes
    private Body bodyHit = null;
    private int bodyHitId = -1;
    private int option = -1;
    private int btn = -1;

    // selecoes
    private boolean isSomeoneSelected = false;
    private boolean cancelSelect = false;


    public CourtScreen() {
        super();

        // screen helper
        screenHelper = new ScreenHelper();

        // sub menus
        optionsTable.offensiveOptionsDraw();
        offensiveOptions = optionsTable.getOffensiveOptions();
        optionsTable.defensiveOptionsDraw();
        defensiveOptions = optionsTable.getDefensiveOptions();
        optionsTable.tacticCreationOptionsDraw();
        tacticCreationOptions = optionsTable.getTacticCreationOptions();
        optionsTable.saveOptionsDraw();
        saveOptions = optionsTable.getSaveOptions();

        // bounding radius
        float playerBoundingRadius = Gdx.graphics.getWidth() / 48f;
        float ballBoundingRadius = Gdx.graphics.getWidth() / 116f;
        float basketBoundingRadius = 0.1f;

        // inicializacao do cesto
        Vector2 posBasket = new Vector2(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()*6/7f);
        basket = new EmptyAgent(world, posBasket, basketBoundingRadius);

        float i = 2;
        for (int n = 1; n <= 5; n++) {
            Vector2 posO = new Vector2(Gdx.graphics.getWidth()*i/10f, Gdx.graphics.getHeight()/10f);
            offense[n-1] = new PlayerO(world, posO, playerBoundingRadius, n == 1, n, screenHelper.getMenuScreen().getOffenseColor());
            offenseNs[n-1] = new PlayerNumber(world, posO, playerBoundingRadius, n, BodyDef.BodyType.DynamicBody, offense[n-1].numberColor());
            Vector2 posD = new Vector2(Gdx.graphics.getWidth()*i/10f,Gdx.graphics.getHeight()*9/10f);
            defense[n-1] = new PlayerD(world, posD, playerBoundingRadius, n, screenHelper.getMenuScreen().getDefenseColor());
            defenseNs[n-1] = new PlayerNumber(world, posD, playerBoundingRadius, n, BodyDef.BodyType.DynamicBody, defense[n-1].numberColor());
            i += 1.75f;
        }

        // inicializacao da bola e respetivo comportamento
        Vector2 posBall = offense[0].getBody().getPosition().add(new Vector2(playerBoundingRadius, playerBoundingRadius));
        ball = new Ball(world, posBall, ballBoundingRadius, offense[0].getTarget(), screenHelper.getMenuScreen().getBallColor());
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

            Box2dRadiusProximity proximity = new Box2dRadiusProximity(playerO, world, playerO.getBoundingRadius()*3f);
            CollisionAvoidance<Vector2> collisionAvoid = new CollisionAvoidance<>(playerO, proximity);
            collisionAvoid.setEnabled(true);

            BlendedSteering<Vector2> behaviours = new BlendedSteering<>(playerO)
                    .add(arriveBO, 1f)
                    .add(collisionAvoid, 1f);

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

            Box2dRadiusProximity proximity = new Box2dRadiusProximity(playerD, world, playerD.getBoundingRadius()*1.5f);
            CollisionAvoidance<Vector2> collisionAvoid = new CollisionAvoidance<>(playerD, proximity);

            BlendedSteering<Vector2> behaviours = new BlendedSteering<>(playerD)
                    .add(arriveBD, 1f)
                    .add(collisionAvoid, 1f);

            playerD.setBehaviour(behaviours);
        }

        // input processor
        Gdx.input.setInputProcessor(gestureDetector);
    }

    @Override
    public void buildScreen(float delta) {
        // cor de fundo
        Gdx.gl.glClearColor(elColor[0], elColor[1], elColor[2], 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // reproducao da jogada
        if (permissionToPlay)
            playTactic();

        // ecras de erro/sucesso no load/save de ficheiros
        if (saveLoad.isFail()) {
            currentDialog = D_FAIL;
            failDialog.setShowing(true);
            Gdx.input.setInputProcessor(failDialog.getStage());
        } else if (saveLoad.isSuccess()) {
            currentDialog = D_SUCCESS;
            successDialog.setShowing(true);
            Gdx.input.setInputProcessor(successDialog.getStage());
        }

        // se algum menu foi, ou nao, seleccionado
        if (screenHelper.isActivated()) {
            screenHelper.screenDraw();
            setPlayersColor(screenHelper.getMenuScreen().getOffenseColor(), screenHelper.getMenuScreen().getDefenseColor());
            ball.setBallColor(screenHelper.getMenuScreen().getBallColor());
            setCourtColor(screenHelper.getMenuScreen().getCourtColor());
            Gdx.input.setInputProcessor(screenHelper.getStage());
        } else {
            // background
            AssetLoader.batch.begin();
            AssetLoader.batch.draw(courtColor(), Gdx.graphics.getWidth()/10f, 0, Gdx.graphics.getWidth()*9/10f, Gdx.graphics.getHeight());
            AssetLoader.batch.end();

            // updates agents on render
            basket.update(delta);
            for (PlayerO playerO : offense) playerO.update(delta);
            for (PlayerD playerD : defense) playerD.update(delta);
            ball.update(delta);

            // agents numbers
            for (int n = 0; n < offenseNs.length; n++) {
                offenseNs[n].setPosition(offense[n].getPosition().cpy());
                offenseNs[n].update(delta);
            }
            for (int n = 0; n < defenseNs.length; n++) {
                defenseNs[n].setPosition(defense[n].getPosition().cpy());
                defenseNs[n].update(delta);
            }

            // updates sub menus
            if (optionsTable.getCurrentOption() == OptionsTable.OFFE) {
                optionsTable.update(offensiveOptions);
            } else if (optionsTable.getCurrentOption() == OptionsTable.DEFE) {
                optionsTable.update(defensiveOptions);
            } else if (optionsTable.getCurrentOption() == OptionsTable.TACT) {
                optionsTable.update(tacticCreationOptions);
            } else if (optionsTable.getCurrentOption() == OptionsTable.SAVE) {
                optionsTable.update(saveOptions);
            }

            // dialogs
            if (dialogIntro.isShowing()) {
                dialogIntro.dialogStageDraw();
                Gdx.input.setInputProcessor(dialogIntro.getStage());
            } else if (currentDialog == D_NOPLAY) {
                if (dialogNoPlay.isShowing()) {
                    dialogNoPlay.dialogStageDraw();
                } else {
                    Gdx.input.setInputProcessor(gestureDetector);
                }
            } else if (currentDialog == D_FILE) {
                if (dialogSaveFile.isShowing()) {
                    dialogSaveFile.dialogStageDraw();
                } else {
                    Gdx.input.setInputProcessor(gestureDetector);
                }
            } else if (currentDialog == D_UPLD) {
                if (dialogLoadFile.isShowing()) {
                    dialogLoadFile.dialogStageDraw();
                } else {
                    if (saveLoad.wasTacticLoaded()) {
                        this.tactic = saveLoad.getTactic();
                        saveLoad.setTacticLoaded(false);
                        Gdx.app.log("TACTIC", "LOADED");
                    }
                    Gdx.input.setInputProcessor(gestureDetector);
                }
            } else if (currentDialog == D_NOTE) {
                if (dialogNotes.isShowing()) {
                    dialogNotes.dialogStageDraw();
                } else {
                    Gdx.input.setInputProcessor(gestureDetector);
                }
            } else if (currentDialog == D_FAIL) {
                if (failDialog.isShowing()) {
                    failDialog.dialogStageDraw();
                } else {
                    Gdx.input.setInputProcessor(gestureDetector);
                }
            } else if (currentDialog == D_SUCCESS) {
                if (successDialog.isShowing()) {
                    successDialog.dialogStageDraw();
                } else {
                    Gdx.input.setInputProcessor(gestureDetector);
                }
            } else {
                Gdx.input.setInputProcessor(gestureDetector);
            }
        }
    }



    /**
     * Metodos auxiliares
     */

    // cor das equipas
    private void setPlayersColor(String colorO, String colorD) {
        for (int o = 0; o < offense.length; o++) {
            offense[o].setPlayerColor(colorO);
            offenseNs[o].setNumColor(offense[o].numberColor());
        }
        for (int d = 0; d < defense.length; d++) {
            defense[d].setPlayerColor(colorD);
            defenseNs[d].setNumColor(defense[d].numberColor());
        }
    }

    // fundo do campo
    private Texture courtColor() {
        switch (this.courtColor) {
            case "Escuro":   return AssetLoader.court_dark;
            case "Claro":    return AssetLoader.court_light;
            case "Verde":    return AssetLoader.court_green;
            case "Azul":     return AssetLoader.court_blue;
            case "Vermelho": return AssetLoader.court_red;
            case "Amarelo":  return AssetLoader.court_yellow;
            default:         return AssetLoader.court_wood;
        }
    }

    private void setCourtColor(String courtColor) { this.courtColor = courtColor; };

    // verifica se o body foi tocado na posicao escolhida
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

    // verifica que botao foi premido
    private int buttonHit() {
        // botoes disponiveis dependem do ecra atual
        /*if (menu.isMenuSelected()) { // TODO apagar o desnecessario
            if (btn == 0) return MENU;
            if (btn == 1) return HELP;
        }*/

        //if (!help.isSelected() || !menu.isHelpSelected()) {
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
                if (btn == 2) return LDFILE;
                if (btn == 3) return NOTES;
                if (btn == 4) return HELP;
            }

        //} else if (btn == 0) return HELP;

        return -1;
    }

    // selecciona um botao num sub menu
    private void tagButton() {
        int curr = optionsTable.getCurrentOption();

        if (curr == OptionsTable.OFFE) {
            offensiveOptions[btn].setIsSelected(true);
            for (OptionButton opt : offensiveOptions) {
                if (opt == offensiveOptions[btn]) {
                    if (btn == FRAME)
                        exec.schedule(() -> opt.setIsSelected(false), 500, TimeUnit.MILLISECONDS);
                } else
                    opt.setIsSelected(false);
            }
        } else if (curr == OptionsTable.DEFE) {
            defensiveOptions[btn].setIsSelected(true);
            for (OptionButton opt : defensiveOptions)
                if (opt != defensiveOptions[btn])
                    opt.setIsSelected(false);
        } else if (curr == OptionsTable.TACT) {
            tacticCreationOptions[btn].setIsSelected(true);
            for (OptionButton opt : tacticCreationOptions) {
                if (opt == tacticCreationOptions[btn])
                    exec.schedule(() -> opt.setIsSelected(false), 500, TimeUnit.MILLISECONDS);
                else
                    opt.setIsSelected(false);
            }
        } else if (curr == OptionsTable.SAVE) {
            Gdx.app.log("SAVE/LOAD", "yeah");
            saveOptions[btn].setIsSelected(true);
            for (OptionButton opt : saveOptions) {
                if (opt != saveOptions[btn])
                    opt.setIsSelected(false);
            }
        }
    }

    // selecciona um player
    private void tagPlayer() {
        if ( ! ((option == PASS || option == SCREEN) && !offense[bodyHitId].hasBall()) ) {
            offense[bodyHitId].setTagged(true);
            isSomeoneSelected = true;
            for (PlayerO playerO : offense) {
                if (playerO != offense[bodyHitId])
                    playerO.setTagged(false);
            }
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

    // verifica se todos os jogadores terminaram a jogada
    private boolean frameOver() {
        boolean over = true;
        for (PlayerO player : offense) over &= player.isFrameOver();
        over &= ball.isFrameOver();
        return over;
    }

    // verifica se todos os jogadores terminaram a jogada
    private void startFrameOver() {
        for (PlayerO player : offense)
            player.setFrameOver(false);
    }

    // reset dos parametros TODO melhorar
    private void reset() {
        for (OptionButton opt : offensiveOptions) {
            if (opt.getIsSelected() && option != FRAME)
                opt.setIsSelected(false);
        }

        btn       = -1;
        option    = -1;
        bodyHitId = -1;
        bodyHit   = null;
        cancelSelect = false;
        isSomeoneSelected = false;
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

        reset();
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

        reset();
    }

    /*/ aplica um bloqueio ao defensor do colega
    private void doAScreen() {
        // TODO caso se consiga implementar o collision avoid

        reset();
    }*/

    // aplica a acao correspondente ao atacante
    private void playerAction(Vector2 posHit) {
        switch (option) {
            case RUN:
                if (bodyHit == null)
                    playerRun(posHit);
                break;
            case PASS:
                if (bodyHit != null)
                    passTheBall();
                break;
            case SCREEN:
                //doAScreen();
                break;
        }
    }

    // adicao de uma nova frame a tatica atual
    private void addNewFrame() {
        // alteracoes necessarias no ecra
        uncheckPlayers(offense);

        // definidas as posicoes iniciais
        if (tactic.getNFrames() == -1) {
            for (int d = 0; d < offense.length; d++) {
                Vector2 posInit = offense[d].getBody().getPosition().cpy();
                tactic.addInitialPos(d, posInit);
                if (offense[d].hasBall()) firstPlayerWithBall = d;
            }

            for (int d = 0; d < defense.length; d++) {
                defense[d].setPlayerWithBall(offense[firstPlayerWithBall]);
                defense[d].setInitMainTargetPosition();
                Vector2 posInit = defense[d].getMainTargetPosition();
                tactic.addInitialPos(d+5, posInit);
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
        if (!defense[0].getPermissionToFollow()) {
            for (PlayerD player : defense)
                player.setPermissionToFollow(true);
        }

        reset();
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
        reset();
        waitTime(1); // thread stuff
    }

    // reproduz a tatica atual
    private void playTactic() {
        if (tactic.initialPos != null && tactic.getSize() > 0 && tactic.getNFrames() > 0) {
            // da permissao aos defesas para adaptar a posicao
            for (PlayerD player : defense)
                player.setPermissionToFollow(true);

            // percorre lista de frames (de cinco em cinco) e aplica movimentos
            exec.schedule(() -> {
                stillPlayingMove = true;

                for (int f = flag - 5; f < flag; f++) {
                    applyMove(f, tactic.getEntryKey(f)[0], tactic.getEntryKey(f)[1]);
                }

                waitTime(7500); // TODO tentar substituir tempo por frameOver (playerO e ball)
                flag += 5;
                startFrameOver();

            }, 2, TimeUnit.SECONDS);

            tactic.setNFrames(tactic.getNFrames() - 1);

            // verifica a situacao da lista
            if (tactic.getNFrames() <= 0) {
                permissionToPlay = false;
                stillPlayingMove = false;
                reset();
            }
        } else reset();
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
            playerD.setPosToInitial();
            playerD.setPlayerWithBall(offense[firstPlayerWithBall]);
        }

        reset();
        uncheckPlayers(offense);

        offense[firstPlayerWithBall].setHasBall(true);
        ball.setPlayerToFollow(offense[firstPlayerWithBall].getTarget());
        ball.setAtPosition(ball.getInitialPos());
    }

    // verifica no que tocou
    private void whatDidITouch(Vector2 posHit) {
        bodyHit = null;

        int curr = optionsTable.getCurrentOption();

        // verifica se carregou num botao das opcoes de um sub menu, do menu ou do help
        /*if (menu.isMenuSelected()) setBtn(menu.getMenuOptions(), posHit);
        else if (help.isSelected() || menu.isHelpSelected()) setBtn(help.getHelpOptions(), posHit);
        else*/ if (curr == OptionsTable.OFFE) setBtn(offensiveOptions, posHit);
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

    // toque no ecra
    private void oneTap(Vector2 posHit) {
        // verifica se foi um botao novo
        if (option != btn) {
            option = buttonHit();
            tagButton();

            // reproducao da tatica
            if (option == PLAY && tactic.getSize() > 0) {
                tactic.setNFrames(tactic.getSize() / 5);
                uncheckPlayers(offense);
                flag = 5;

                // coloca players e bola na posicao inicial
                for (int p = 0; p < tactic.initialPos.length/2; p++) {
                    Vector2 init = tactic.initialPos[p];
                    offense[p].setTargetPosition(init);
                    offense[p].setAtPosition(init);
                }
                for (int p = tactic.initialPos.length/2; p < tactic.initialPos.length; p++) {
                    Vector2 init = tactic.initialPos[p];
                    defense[p-5].setMainTargetPosition(init);
                    defense[p-5].setAtPosition(init);
                }

                // posse de bola no inicio da jogada
                offense[firstPlayerWithBall].setHasBall(true);
                ball.setPlayerToFollow(offense[firstPlayerWithBall].getTarget());
                ball.setAtPosition(offense[firstPlayerWithBall].getInitialPos());
                for (PlayerO player : offense)
                    if (!player.equals(offense[firstPlayerWithBall]))
                        player.setHasBall(false);
                for (PlayerD player : defense)
                    player.setPlayerWithBall(offense[firstPlayerWithBall]);

                // da permissao para reproduzir a jogada
                permissionToPlay = true;
            }

            // se nao houver permissao para reproduzir
            else if (option == PLAY && !permissionToPlay) {
                currentDialog = D_NOPLAY;

                dialogNoPlay.dialogDraw();
                dialogNoPlay.setShowing(true);

                Gdx.input.setInputProcessor(dialogNoPlay.getStage());
                reset();
            }

            // adicao de uma nova frame
            else if (option == FRAME) addNewFrame();

            // limpeza da tatica
            else if (option == RESET) resetTactic();

            // caso o botao seja para mudar de ecra
            else if (option == MENU) {
                reset();
                screenHelper.setMenuSelected(true);
                screenHelper.getMenuScreen().setSelected();
                Gdx.input.setInputProcessor(screenHelper.getMenuScreen().getStage());
            } else if (option == HELP) {
                reset();
                screenHelper.setHelpSelected(true);
                screenHelper.getHelpScreen().setSelected();
                Gdx.input.setInputProcessor(screenHelper.getHelpScreen().getStage());
            }

            // tab para guardar ficheiro da tatica
            else if (option == SVFILE) {
                currentDialog = D_FILE;
                dialogSaveFile.dialogDraw();
                dialogSaveFile.setShowing(true);

                Gdx.input.setInputProcessor(dialogSaveFile.getStage());
            }

            // tab para carregar tatica
            else if (option == LDFILE) { // TODO load
                currentDialog = D_UPLD;
                dialogLoadFile.dialogDraw();
                dialogLoadFile.setShowing(true);

                Gdx.input.setInputProcessor(dialogLoadFile.getStage());
            }

            // tab para tirar notas
            else if (option == NOTES) {
                currentDialog = D_NOTE;
                dialogNotes.dialogDraw();
                dialogNotes.setShowing(true);

                Gdx.input.setInputProcessor(dialogNotes.getStage());
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

        // iniciar movimento
        playerAction(posHit);
    }


    @Override
    // verifica contagem do numero de taps
    public boolean tap(float x, float y, int count, int button) {
        final Vector2 posHit = new Vector2(x, Gdx.graphics.getHeight() - y);
        whatDidITouch(posHit);
        trueCounter = count;

        if (trueCounter == 2) {
            if (btn == MENU) {
                int opt = optionsTable.getCurrentOption();

                if (opt == OptionsTable.OFFE) {
                    optionsTable.setCurrentOption(OptionsTable.DEFE);
                    elColor[0] = 180 / 255f;
                    elColor[1] = 18 / 255f;
                    elColor[2] = 0;
                } else if (opt == OptionsTable.DEFE) {
                    optionsTable.setCurrentOption(OptionsTable.TACT);
                    elColor[0] = 200 / 255f;
                    elColor[1] = 80 / 255f;
                    elColor[2] = 0;
                } else if (opt == OptionsTable.TACT) {
                    optionsTable.setCurrentOption(OptionsTable.SAVE);
                    elColor[0] = 50 / 255f;
                    elColor[1] = 150 / 255f;
                    elColor[2] = 50 / 255f;
                } else if (opt == OptionsTable.SAVE) {
                    optionsTable.setCurrentOption(OptionsTable.OFFE);
                    elColor[0] = 0;
                    elColor[1] = 120 / 255f;
                    elColor[2] = 200 / 255f;
                }

                trueCounter = 0;

                reset();
            }
        } else {
            exec.schedule(() -> {
                if (trueCounter == 1) {
                    oneTap(posHit);
                    trueCounter = 0;
                }
            }, 180, TimeUnit.MILLISECONDS);
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
        dialogNoPlay.dispose();
        dialogSaveFile.dispose();
        // TODO verificar os que estao por colocar aqui
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

}

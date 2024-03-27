package main;

import checker.Checker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import checker.CheckerConstants;
//import fileio.Input;
import fileio.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
//import java.util.Objects;
import java.sql.SQLOutput;
import java.util.*;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * DO NOT MODIFY MAIN METHOD
     * Call the checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(CheckerConstants.TESTS_PATH);
        Path path = Paths.get(CheckerConstants.RESULT_PATH);

        if (Files.exists(path)) {
            File resultFile = new File(String.valueOf(path));
            for (File file : Objects.requireNonNull(resultFile.listFiles())) {
                file.delete();
            }
            resultFile.delete();
        }
        Files.createDirectories(path);

        for (File file : Objects.requireNonNull(directory.listFiles())) {
            String filepath = CheckerConstants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getName(), filepath);
            }
        }

        Checker.calculateScore();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Input inputData = objectMapper.readValue(new File(CheckerConstants.TESTS_PATH + filePath1),
                Input.class);

        ArrayNode output = objectMapper.createArrayNode();

        //TODO add here the entry point to your implementation
        //        DecksInput playerOneDecks = inputData.getPlayerOneDecks();
//        DecksInput playerTwoDecks = inputData.getPlayerTwoDecks();
//        ArrayList<ArrayList<CardInput>> decks_1 = new ArrayList<ArrayList<CardInput>>();
//        ArrayList<ArrayList<CardInput>> decks_2 = new ArrayList<ArrayList<CardInput>>();
//        playerOneDecks.setDecks(decks_1);
//        playerTwoDecks.setDecks(decks_2);

        int zero = 0, one = 1, two = 2, three = 3, four = 4,
                five = 5, ten = 10, thirty = 30;

        ArrayList<GameInput> games = inputData.getGames();

        GameInput gameInput0 = games.get(zero);

        StartGameInput startGameInput0 = gameInput0.getStartGame();

        ArrayList<ActionsInput> actions = gameInput0.getActions();
        ActionsInput action;
        int actionsSize = actions.size();

        int startingPlayer = startGameInput0.getStartingPlayer();
        int player = startingPlayer;

        int playerOneDeckIdx = startGameInput0.getPlayerOneDeckIdx();
        int playerTwoDeckId = startGameInput0.getPlayerTwoDeckIdx();

        DecksInput playerOneDecks = inputData.getPlayerOneDecks();
        DecksInput playerTwoDecks = inputData.getPlayerTwoDecks();

        ArrayList<ArrayList<CardInput>> decks1 = playerOneDecks.getDecks();
        ArrayList<ArrayList<CardInput>> decks2 = playerTwoDecks.getDecks();

        ArrayList<CardInput> playerOneDeck = decks1.get(playerOneDeckIdx);
        ArrayList<CardInput> playerTwoDeck = decks2.get(playerTwoDeckId);

        CardInput playerOneHero = startGameInput0.getPlayerOneHero();
        CardInput playerTwoHero = startGameInput0.getPlayerTwoHero();

        int shuffleSeed = startGameInput0.getShuffleSeed();
        Random random = new Random(shuffleSeed);

        Collections.shuffle(playerOneDeck, random);
        random = new Random(shuffleSeed);
        Collections.shuffle(playerTwoDeck, random);

        CardInput playerOneZero = playerOneDeck.get(zero);
        CardInput playerTwoZero = playerTwoDeck.get(zero);

        ArrayList<CardInput> handOne = new ArrayList<>();
        handOne.add(playerOneZero);
        ArrayList<CardInput> handTwo = new ArrayList<>();
        handTwo.add(playerTwoZero);

        playerOneDeck.remove(zero);
        playerTwoDeck.remove(zero);

        int playerOneMana = one, playerTwoMana = one;

        int manaOne = two, manaTwo = two;

        ArrayList<ArrayList<CardInput>> table = new ArrayList<>();
        ArrayList<CardInput> row0 = new ArrayList<>(),
                row1 = new ArrayList<>(),
                row2 = new ArrayList<>(),
                row3 = new ArrayList<>();
        table.add(row0);
        table.add(row1);
        table.add(row2);
        table.add(row3);
        int[][] frozen = new int[four][five];
        for (int abc = zero; abc < four; abc++) {
            for (int cba = zero; cba < five; cba++) {
                frozen[abc][cba] = zero;
            }
        }
        for (int i = zero; i < actionsSize; i++) {
            action = actions.get(i);
            String command = action.getCommand();
            int handIdx = action.getHandIdx();
            if (player > two) {
                player = player - two;
            }
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("command", command);
            if (command.equals("getPlayerDeck")
                    || command.equals("getCardsInHand")) {
                ArrayNode deck = objectMapper.createArrayNode();
                int playerIdx = action.getPlayerIdx();
                objectNode.put("playerIdx", playerIdx);
                int size;
                if (command.equals("getPlayerDeck")) {
                    if (playerIdx == one) {
                        size = playerOneDeck.size();
                    } else {
                        size = playerTwoDeck.size();
                    }
                } else {
                    if (playerIdx == one) {
                        size = handOne.size();
                    } else {
                        size = handTwo.size();
                    }
                }
                for (int j = zero; j < size; j++) {
                    ObjectNode auxNode = objectMapper.createObjectNode();
                    CardInput cardInputJ;
                    if (command.equals("getPlayerDeck")) {
                        if (playerIdx == one) {
                            cardInputJ = playerOneDeck.get(j);
                        } else {
                            cardInputJ = playerTwoDeck.get(j);
                        }
                    } else {
                        if (playerIdx == one) {
                            cardInputJ = handOne.get(j);
                        } else {
                            cardInputJ = handTwo.get(j);
                        }
                    }
                    ArrayList<String> colors = cardInputJ.getColors();
                    int colorSize = colors.size();
                    String colorK;
                    ArrayNode colorsArray = objectMapper.createArrayNode();
                    for (int k = zero; k < colorSize; k++) {
                        colorK = colors.get(k);
                        colorsArray.add(colorK);
                    }
                    int mana = cardInputJ.getMana();
                    auxNode.put("mana", mana);
                    String name = cardInputJ.getName();
                    if ((!name.equals("Firestorm"))
                            && (!name.equals("Winterfell"))
                            && (!name.equals("Heart Hound"))) {
                        int damage = cardInputJ.getAttackDamage();
                        int health = cardInputJ.getHealth();
                        auxNode.put("attackDamage", damage);
                        auxNode.put("health", health);
                    }
                    String description = cardInputJ.getDescription();
                    auxNode.put("description", description);
                    auxNode.set("colors", colorsArray);
                    auxNode.put("name", name);
                    deck.add(auxNode);
                }
                objectNode.set("output", deck);
                output.add(objectNode);
            } else {
                if (command.equals("getPlayerHero")) {
                    int playerIdx = action.getPlayerIdx();
                    CardInput cardInput;
                    if (playerIdx == one) {
                        cardInput = playerOneHero;
                    } else {
                        cardInput = playerTwoHero;
                    }
                    int mana = cardInput.getMana();
                    String description = cardInput.getDescription();
                    ArrayList<String> colors = cardInput.getColors();
                    String name = cardInput.getName();
                    ArrayNode colorsArray = objectMapper.createArrayNode();
                    int colorSize = colors.size();
                    String colorK;
                    for (int k = zero; k < colorSize; k++) {
                        colorK = colors.get(k);
                        colorsArray.add(colorK);
                    }
                    ObjectNode hero = objectMapper.createObjectNode();
                    objectNode.put("playerIdx", playerIdx);
                    hero.put("mana", mana);
                    hero.put("description", description);
                    hero.set("colors", colorsArray);
                    hero.put("name", name);
                    cardInput.setHealth(thirty);
                    int health = cardInput.getHealth();
                    hero.put("health", health);
                    objectNode.set("output", hero);

                    output.add(objectNode);
                } else {
                    if (command.equals("getPlayerTurn")) {
                        objectNode.put("output", player);
                        output.add(objectNode);
                    } else {
                        if (command.equals("placeCard")) {
                            ArrayList<CardInput> playerHand;
                            if (player == 1) {
                                playerHand = handOne;
                            } else {
                                playerHand = handTwo;
                            }
                            CardInput handIdxAux = playerHand.get(handIdx);
                            String cardInputName = handIdxAux.getName();
                            if (cardInputName.equals("Heart Hound")
                                    || cardInputName.equals("Winterfell")
                                    || cardInputName.equals("Firestorm")) {
                                objectNode.put("handIdx", handIdx);
                                String error = "Cannot place environment card on table.";
                                objectNode.put("error", error);
                                output.add(objectNode);
                            } else {
                                int cardMana = handIdxAux.getMana();
                                if ((player == one) && (playerOneMana < cardMana)) {
                                    objectNode.put("handIdx", handIdx);
                                    String error = "Not enough mana to place card on table.";
                                    objectNode.put("error", error);
                                    output.add(objectNode);
                                } else {
                                    if ((player == two) && (playerTwoMana < cardMana)) {
                                        objectNode.put("handIdx", handIdx);
                                        String error = "Not enough mana to place card on table.";
                                        objectNode.put("error", error);
                                        output.add(objectNode);
                                    } else {
                                        ArrayList<CardInput> rowAux;
                                        if (cardInputName.equals("The Ripper")
                                                || cardInputName.equals("Miraj")
                                                || cardInputName.equals("Goliath")
                                                || cardInputName.equals("Warden")) {
                                            rowAux = table.get(player % two + one);
                                        } else {
                                            if (player == one) {
                                                rowAux = table.get(player + two);
                                            } else {
                                                rowAux = table.get(player - two);
                                            }
                                        }
                                        int rowAuxSize = rowAux.size();
                                        if (rowAuxSize > four) {
                                            String error =
                                                    "Cannot place card on table since row is full.";
                                            objectNode.put("error", error);
                                            objectNode.put("handIdx", handIdx);
                                            output.add(objectNode);
                                        } else {
                                            rowAux.add(handIdxAux);
                                            if (player == one) {
                                                handOne.remove(handIdx);
                                                playerOneMana -= handIdxAux.getMana();
                                            } else {
                                                handTwo.remove(handIdx);
                                                playerTwoMana -= handIdxAux.getMana();
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            if (command.equals("endPlayerTurn")) {
                                if (player == one) {
                                    playerOneMana = playerOneMana + manaOne;
                                    boolean empty = playerOneDeck.isEmpty();
                                    if (empty == false) {
                                        CardInput playerOneZeroAux = playerOneDeck.get(zero);
                                        handOne.add(playerOneZeroAux);
                                        playerOneDeck.remove(zero);
                                    }
                                    if (manaOne < ten) {
                                        manaOne = manaOne + one;
                                    }
                                } else {
                                    boolean empty = playerTwoDeck.isEmpty();
                                    playerTwoMana = playerTwoMana + manaTwo;
                                    if (empty == false) {
                                        CardInput playerTwoZeroAux = playerTwoDeck.get(zero);
                                        handTwo.add(playerTwoZeroAux);
                                        playerTwoDeck.remove(zero);
                                    }
                                    if (manaTwo < ten) {
                                        manaTwo = manaTwo + one;
                                    }
                                }
                                player = player + one;
                            } else {
                                if (command.equals("getPlayerMana")) {
                                    int playerIdx = action.getPlayerIdx();
                                    objectNode.put("playerIdx", playerIdx);
                                    if (playerIdx == one) {
                                        int mana = playerOneMana;
                                        objectNode.put("output", mana);
                                    } else {
                                        int mana = playerTwoMana;
                                        objectNode.put("output", mana);
                                    }
                                    output.add(objectNode);
                                } else {
                                    if (command.equals("getCardsOnTable")) {
                                        ArrayNode arrayNode = objectMapper.createArrayNode();
                                        for (int a = zero; a < four; a++) {
                                            ArrayList<CardInput> row = table.get(a);
                                            int size = row.size();
                                            ArrayNode deck = objectMapper.createArrayNode();
                                            for (int b = zero; b < size; b++) {
                                                ObjectNode auxNode =
                                                        objectMapper.createObjectNode();
                                                CardInput cardInput = row.get(b);
                                                ArrayList<String> colors = cardInput.getColors();
                                                int colorsSize = colors.size();
                                                ArrayNode colorsArray =
                                                        objectMapper.createArrayNode();
                                                for (int c = zero; c < colorsSize; c++) {
                                                    String color = colors.get(c);
                                                    colorsArray.add(color);
                                                }
                                                int mana = cardInput.getMana();
                                                auxNode.put("mana", mana);
                                                String name = cardInput.getName();
                                                if ((!name.equals("Firestorm"))
                                                        && (!name.equals("Winterfell"))
                                                        && (!name.equals("Heart Hound"))) {
                                                    int damage = cardInput.getAttackDamage();
                                                    int health = cardInput.getHealth();
                                                    auxNode.put("attackDamage", damage);
                                                    auxNode.put("health", health);
                                                }
                                                String description = cardInput.getDescription();
                                                auxNode.put("description", description);
                                                auxNode.set("colors", colorsArray);
                                                auxNode.put("name", name);
                                                deck.add(auxNode);
                                            }
                                            arrayNode.add(deck);
                                        }
                                        objectNode.set("output", arrayNode);
                                        output.add(objectNode);
                                    } else { //de aici
        if (command.equals("getEnvironmentCardsInHand")) { //comanda unu
            int playerIdx = action.getPlayerIdx();
            objectNode.put("playerIdx", playerIdx);
            ArrayNode arrayNode = objectMapper.createArrayNode();
            ArrayNode deck = objectMapper.createArrayNode();
            objectNode.put("playerIdx", playerIdx);
            int size;
            if (playerIdx == one) {
                size = handOne.size();
            } else {
                size = handTwo.size();
            }
            for (int j = zero; j < size; j++) {
                ObjectNode auxNode = objectMapper.createObjectNode();
                CardInput cardInputJ;
                if (playerIdx == one) {
                    cardInputJ = handOne.get(j);
                } else {
                    cardInputJ = handTwo.get(j);
                }
                String name = cardInputJ.getName();
                if (name.equals("Firestorm")
                        || name.equals("Winterfell")
                        || name.equals("Heart Hound")) {
                    ArrayList<String> colors =
                            cardInputJ.getColors();
                    int colorSize = colors.size();
                    String colorK;
                    ArrayNode colorsArray = objectMapper.createArrayNode();
                    for (int k = zero; k < colorSize; k++) {
                        colorK = colors.get(k);
                        colorsArray.add(colorK);
                    }
                    int mana = cardInputJ.getMana();
                    auxNode.put("mana", mana);
                    String description = cardInputJ.getDescription();
                    auxNode.put("description", description);
                    auxNode.set("colors", colorsArray);
                    auxNode.put("name", name);
                    deck.add(auxNode);
                }
            }
            objectNode.set("output", deck);
            output.add(objectNode);
        } else {
            if (command.equals("useEnvironmentCard")) { //comanda 2
//                    System.out.println(playerOneMana);
//                    System.out.println(playerTwoMana);
//                    System.out.println();
                int affectedRow = action.getAffectedRow();
                ArrayList<CardInput> playerHand;
                if (player == 1) {
                    playerHand = handOne;
                } else {
                    playerHand = handTwo;
                }
                CardInput cardInputJ = playerHand.get(handIdx);
                String name = cardInputJ.getName();
                if ((!name.equals("Heart Hound"))
                        && (!name.equals("Winterfell"))
                        && (!name.equals("Firestorm"))) {
                    objectNode.put("handIdx", handIdx);
                    objectNode.put("affectedRow", affectedRow);
                    String error =
                            "Chosen card is not of type environment.";
                    objectNode.put("error", error);
                    output.add(objectNode);
                } else {
                    CardInput handIdxAux = playerHand.get(handIdx);
                    int cardMana = handIdxAux.getMana();
                    if (((player == one)
                            && (playerOneMana < cardMana))
                            || ((player == two)
                            && (playerTwoMana < cardMana))) {
                        objectNode.put("handIdx", handIdx);
                        objectNode.put("affectedRow", affectedRow);
                        String error =
                                "Not enough mana to place card on table.";
                        objectNode.put("error", error);
                        output.add(objectNode);
                    } else {
                        if ((affectedRow < two && player == two)
                                || (affectedRow > one
                                && player == one)) {
                            objectNode.put("handIdx", handIdx);
                            objectNode.put("affectedRow", affectedRow);
                            String error =
                                    "Chosen row does not belong to the enemy.";
                            objectNode.put("error", error);
                            output.add(objectNode);
                        } else {
                            if (name.equals("Heart Hound")) {
                                if (player == one) {
                                    ArrayList<CardInput> line = table.get(three);
                                    int lineSize = line.size();
                                    if (lineSize > four && affectedRow == zero) {
                                        objectNode.put("handIdx", handIdx);
                                        objectNode.put("affectedRow",
                                                affectedRow);
                                        String error =
                                                "Cannot steal enemy card since the player's row is full.";
                                        objectNode.put("error", error);
                                        output.add(objectNode);
                                        continue;
                                    }
                                    line = table.get(two);
                                    lineSize = line.size();
                                    if (lineSize > four && affectedRow == 1) {
                                        objectNode.put("handIdx", handIdx);
                                        objectNode.put("affectedRow",
                                                affectedRow);
                                        String error =
                                                "Cannot steal enemy card since the player's row is full.";
                                        objectNode.put("error", error);
                                        output.add(objectNode);
                                        continue;
                                    }
                                    ArrayList<CardInput> affectedInput =
                                            table.get(affectedRow);
                                    int affectedSize = affectedInput.size();
                                    int hp = zero, swap = zero;
                                    for (int a = zero; a < affectedSize; a++) {
                                        CardInput auxCardInput = line.get(a);
                                        int health = auxCardInput.getHealth();
                                        if (hp < health) {
                                            swap = a;
                                            hp = health;
                                        }
                                    }
                                    CardInput swapCard = affectedInput.get(swap);
                                    if (affectedRow == one) {
                                        int notAffectedRow = affectedRow + one;
                                        ArrayList<CardInput> notAffectedInput =
                                                table.get(notAffectedRow);
                                        notAffectedInput.add(swapCard);
                                    } else {
                                        int notAffectedRow = affectedRow + three;
                                        ArrayList<CardInput> notAffectedInput =
                                                table.get(notAffectedRow);
                                        notAffectedInput.add(swapCard);
                                    }
                                    affectedInput.remove(swap);
                                    playerOneMana = playerOneMana - cardMana;
                                    playerHand.remove(handIdx);
                                }
                                if (player == two) {
                                    ArrayList<CardInput> line = table.get(one);
                                    int lineSize = line.size();
                                    if (lineSize > four && affectedRow == two) {
                                        objectNode.put("handIdx", handIdx);
                                        objectNode.put("affectedRow",
                                                affectedRow);
                                        String error =
                                                "Cannot steal enemy card since the player's row is full.";
                                        objectNode.put("error", error);
                                        output.add(objectNode);
                                        continue;
                                    }
                                    line = table.get(zero);
                                    lineSize = line.size();
                                    if (lineSize > four && affectedRow == three) {
                                        objectNode.put("handIdx", handIdx);
                                        objectNode.put("affectedRow",
                                                affectedRow);
                                        String error =
                                                "Cannot steal enemy card since the player's row is full.";
                                        objectNode.put("error", error);
                                        output.add(objectNode);
                                        continue;
                                    }
                                    ArrayList<CardInput> affectedInput =
                                            table.get(affectedRow);
                                    int affectedSize = affectedInput.size();
                                    int hp = zero, swap = zero;
                                    for (int a = zero; a < affectedSize; a++) {
                                        CardInput auxCardInput = line.get(a);
                                        int health = auxCardInput.getHealth();
                                        if (hp < health) {
                                            swap = a;
                                            hp = health;
                                        }
                                    }
                                    CardInput swapCard = affectedInput.get(swap);
                                    if (affectedRow == two) {
                                        int notAffectedRow = affectedRow - one;
                                        ArrayList<CardInput> notAffectedInput =
                                                table.get(notAffectedRow);
                                        notAffectedInput.add(swapCard);
                                    } else {
                                        int notAffectedRow = affectedRow - three;
                                        ArrayList<CardInput> notAffectedInput =
                                                table.get(notAffectedRow);
                                        notAffectedInput.add(swapCard);
                                    }
                                    affectedInput.remove(swap);
                                    playerTwoMana = playerTwoMana - cardMana;
                                    playerHand.remove(handIdx);
                                }
                            }
                            if (name.equals("Winterfell")) {
                                ArrayList<CardInput> affectedInput =
                                        table.get(affectedRow);
                                int affectedSize = affectedInput.size();
                                for (int a = zero; a < affectedSize; a++) {
                                    frozen[affectedRow][a] = one;
                                }
                                if (player == one) {
                                    playerOneMana = playerOneMana - cardMana;
                                } else {
                                    playerTwoMana = playerTwoMana - cardMana;
                                }
                                playerHand.remove(handIdx);
                            }
                            if (name.equals("Firestorm")) {
                                ArrayList<CardInput> affectedInput =
                                        table.get(affectedRow);
                                int affectedSize = affectedInput.size();
                                for (int a = affectedSize - one; a >= zero; a--) {
                                    CardInput getAffected = affectedInput.get(a);
                                    int health = getAffected.getHealth();
                                    health = health - one;
                                    getAffected.setHealth(health);
                                    health = getAffected.getHealth();
                                    if (health < one) {
                                        affectedInput.remove(a);
                                    }
                                }
                                if (handIdx == one) {
                                    playerOneMana = playerOneMana - cardMana;
                                } else {
                                    playerTwoMana = playerTwoMana - cardMana;
                                }
                                playerHand.remove(handIdx);
                            }
                        }
                    }
                }
            } else {
                if (command.equals("getCardAtPosition")) { //comanda 3
                    int x = action.getX(), y = action.getY();
                    objectNode.put("x", x);
                    objectNode.put("y", y);
                    ArrayList<CardInput> line = table.get(x);
                    int size = line.size();
                    size--;
                    if (size < y) {
                        String error =
                                "No card available at that position.";
                        objectNode.put("output", error);
                    } else {
                        CardInput cardInputJ = line.get(y);
                        ObjectNode auxNode =
                                                                objectMapper.createObjectNode();
                                                        ArrayList<String> colors =
                                                                cardInputJ.getColors();
                                                        String name = cardInputJ.getName();
                                                        int colorSize = colors.size();
                                                        String colorK;
                                                        ArrayNode colorsArray =
                                                                objectMapper.createArrayNode();
                                                        for (int k = zero; k < colorSize; k++) {
                                                            colorK = colors.get(k);
                                                            colorsArray.add(colorK);
                                                        }
                                                        int mana = cardInputJ.getMana();
                                                        auxNode.put("mana", mana);
                                                        int attackDamage = cardInputJ.getAttackDamage();
                                                        auxNode.put("attackDamage", attackDamage);
                                                        int health = cardInputJ.getHealth();
                                                        auxNode.put("health", health);
                                                        String description = cardInputJ.getDescription();
                                                        auxNode.put("description", description);
                                                        auxNode.set("colors", colorsArray);
                                                        auxNode.put("name", name);
                                                        objectNode.set("output", auxNode);
                                                    }
                                                    output.add(objectNode);
                                                } else {
                                                    if (command.equals("getFrozenCardsOnTable")) {
                                                        ArrayNode arrayNode = objectMapper.createArrayNode();
                                                        for (int a = zero; a < four; a++) {
                                                            ArrayList<CardInput> row = table.get(a);
                                                            int size = row.size();
                                                            ArrayNode deck = objectMapper.createArrayNode();
                                                            if (size > 0) {
                                                                for (int b = zero; b < size; b++) {
                                                                    ObjectNode auxNode;
                                                                    CardInput cardInput;
                                                                    if (frozen[a][b] != 0) {
                                                                        auxNode = objectMapper.createObjectNode();
                                                                        cardInput = row.get(b);
                                                                        ArrayList<String> colors = cardInput.getColors();
                                                                        int colorsSize = colors.size();
                                                                        ArrayNode colorsArray =
                                                                                objectMapper.createArrayNode();
                                                                        for (int c = zero; c < colorsSize; c++) {
                                                                            String color = colors.get(c);
                                                                            colorsArray.add(color);
                                                                        }
                                                                        int mana = cardInput.getMana();
                                                                        auxNode.put("mana", mana);
                                                                        String name = cardInput.getName();
                                                                        if ((!name.equals("Firestorm"))
                                                                                && (!name.equals("Winterfell"))
                                                                                && (!name.equals("Heart Hound"))) {
                                                                            int damage = cardInput.getAttackDamage();
                                                                            int health = cardInput.getHealth();
                                                                            auxNode.put("attackDamage", damage);
                                                                            auxNode.put("health", health);
                                                                        }
                                                                        String description = cardInput.getDescription();
                                                                        auxNode.put("description", description);
                                                                        auxNode.set("colors", colorsArray);
                                                                        auxNode.put("name", name);
                                                                        deck.add(auxNode);
                                                                    }
                                                                }
                                                                arrayNode.add(deck);
                                                            }
                                                            arrayNode.add(arrayNode);
                                                        }
                                                        objectNode.set("output", arrayNode);
                                                        output.add(objectNode);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }


        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), output);
    }
}

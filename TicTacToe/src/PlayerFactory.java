public class PlayerFactory {
    Player buildPlayer(String playerArg){
        switch (playerArg){
            case "human":
                return new HumanPlayer();
            case "clever":
                return new CleverPlayer();
            case "whatever":
                return new WhateverPlayer();
            default:
                return null;
        }
    }
}

import java.util.Scanner;

/**
 * The Chat class simulates a chat using ChatterBots. It initializes multiple bots and facilitates
 * a conversation between them. The user progresses the conversation by pressing enter after each bot's reply.
 * The conversation initiates with a predefined seed statement and subsequently continues based on each bot's response.
 *
 * @author Shir Rashkovits
 */
public class Chat {
    static final String SEED_OF_THE_CONVERSATION = "say I love object oriented programing";
    static final String FIRST_CHATTER_BOT_NAME = "shir";
    static final String SECOND_CHATTER_BOT_NAME = "roey";

    /**
     * The main method serves as the entry point for the chat simulation program.
     * It initializes an array of ChatterBots, sets up a seed statement for the conversation,
     * and enters a loop to facilitate the chat. In each iteration of the loop, each ChatterBot
     * in the array responds to the statement from the previous bot. The conversation continues
     * as long as the user keep progressing the chat by pressing enter after each bot's response.
     *
     * @param args Command line arguments (not used in this program).
     */
    public static void main(String[] args) {
        ChatterBot[] bots = initializeChatterBots();

        String statement = SEED_OF_THE_CONVERSATION;
        Scanner scanner = new Scanner(System.in);

        // simulate a chat between the bots by order.
        while(true) {
            for(ChatterBot bot : bots) {
                statement = bot.replyTo(statement);
                outputBotStatement(bot, statement);
                scanner.nextLine(); // wait for the user to press enter to continue the conversation
            }
        }
    }

    /*
     * Outputs the given statement from the specified bot.
     *
     * @param bot The ChatterBot that is making the statement.
     * @param statement The statement made by the bot.
     */
    private static void outputBotStatement(ChatterBot bot, String statement) {
        System.out.print(bot.getName()  + ": " + statement);
    }

    /*
     * Initializes an array of ChatterBots with predefined responses.
     *
     * @return An array of initialized ChatterBots.
     */
    private static ChatterBot[] initializeChatterBots() {
        String[] firstChatterBotLegalResponses = {
                "say " + ChatterBot.PLACEHOLDER_FOR_REQUESTED_PHRASE + "? okay: " + ChatterBot.PLACEHOLDER_FOR_REQUESTED_PHRASE,
                "oop is the best course ever! here you go: " + ChatterBot.PLACEHOLDER_FOR_REQUESTED_PHRASE,
        };
        String[] firstChatterBotIllegalResponses = {
                "what " + ChatterBot.PLACEHOLDER_FOR_ILLEGAL_REQUEST + "?",
                "say " + ChatterBot.PLACEHOLDER_FOR_ILLEGAL_REQUEST,
                "say say I love object oriented programing"
        };

        String[] secondChatterBotLegalResponses = {
                "I agree! " + ChatterBot.PLACEHOLDER_FOR_REQUESTED_PHRASE + " indeed!",
                "here: " + ChatterBot.PLACEHOLDER_FOR_REQUESTED_PHRASE,
        };
        String[] secondChatterBotIllegalResponses = {
                "what so you mean by " + ChatterBot.PLACEHOLDER_FOR_ILLEGAL_REQUEST + "?",
                "say say " + ChatterBot.PLACEHOLDER_FOR_ILLEGAL_REQUEST + " ok?",
                "say say"
        };

        return new ChatterBot[]{
                new ChatterBot(FIRST_CHATTER_BOT_NAME, firstChatterBotLegalResponses, firstChatterBotIllegalResponses),
                new ChatterBot(SECOND_CHATTER_BOT_NAME, secondChatterBotLegalResponses, secondChatterBotIllegalResponses),
        };
    }
}

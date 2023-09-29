import java.util.Scanner;

public class Chat {
    public static void main(String[] args) {
        int[][] arr = new int[3][4];
        for (int i=0; i < arr.length; i++){
            for (int j=0; j < arr[0].length; j++){
                arr[i][j] = 1;
            }
        }

        // Create ChatterBots
        String[] repliesToIllegalRequest = new String[]{
                ChatterBot.ILLEGAL_REQUEST_PLACEHOLDER + " is an illegal request.",
                "You're not following my instructions with telling me " + ChatterBot.ILLEGAL_REQUEST_PLACEHOLDER,
                "say say what you want to say"
        };

        String[] repliesToLegalRequest = new String[]{
                "Sure, I can say: " + ChatterBot.LEGAL_REQUESTED_PLACEHOLDER,
                "Of course, here's your request: " + ChatterBot.LEGAL_REQUESTED_PLACEHOLDER
        };

        ChatterBot shirBot = new ChatterBot("Shir", repliesToLegalRequest, repliesToIllegalRequest);

        repliesToIllegalRequest = new String[]{
                "Why, oh why, are you making an illegal request?", ChatterBot.ILLEGAL_REQUEST_PLACEHOLDER + " is a no-go zone.",
                ChatterBot.ILLEGAL_REQUEST_PLACEHOLDER + " is not a valid request.", "say say statement"
        };

        repliesToLegalRequest = new String[]{
                "Alright, here's your request: " + ChatterBot.LEGAL_REQUESTED_PLACEHOLDER,
                "Sure, here it goes: " + ChatterBot.LEGAL_REQUESTED_PLACEHOLDER
        };

        ChatterBot adiBot = new ChatterBot("Adi", repliesToLegalRequest, repliesToIllegalRequest);

        ChatterBot[] bots = {shirBot, adiBot};

        String statement = "hello";
        Scanner scanner = new Scanner(System.in);


        while (true) {
            for (ChatterBot bot : bots) {
                statement = bot.replyTo(statement);
                System.out.print(bot.getName() + ": " + statement);
                scanner.nextLine(); //wait for “enter” before continuing
            }
        }
    }
}

import java.util.Random;

/**
 * ChatterBot is a class representing a named chatter bot that responds to user statements.
 * It can respond to both legal requests (starting with REQUEST_PREFIX) and illegal requests.
 * The bot generates random responses using provided patterns in construction, then replacing the
 * placeholders in the selected response with the given statement.
 *
 * @author Shir Rashkovits
 */
class ChatterBot {
	static final String REQUEST_PREFIX = "say ";
	static final String PLACEHOLDER_FOR_REQUESTED_PHRASE = "<phrase>";
	static final String PLACEHOLDER_FOR_ILLEGAL_REQUEST = "<request>";

	Random randomGenerator = new Random(); // Random instance for generating random responses
	String name; // The name of the ChatterBot
	String[] repliesToIllegalRequest;
	String[] repliesToLegalRequest;

	/**
	 * Constructs a ChatterBot with a specified name and response patterns for legal and illegal statements.
	 *
	 * @param name                  The name of the ChatterBot.
	 * @param repliesToLegalRequest An array of response patterns for legal requests.
	 *                              It may contain appearances of LEGAL_REQUESTED_PLACEHOLDER.
	 * @param repliesToIllegalRequest An array of response patterns for illegal requests.
	 *                                It may contain appearances of ILLEGAL_REQUEST_PLACEHOLDER.
	 */
	ChatterBot(String name, String[] repliesToLegalRequest, String[] repliesToIllegalRequest) {
		this.name = name;
		this.repliesToLegalRequest = new String[repliesToLegalRequest.length];
		for (int i = 0; i < repliesToLegalRequest.length; i++) {
			this.repliesToLegalRequest[i] = repliesToLegalRequest[i];
		}
		this.repliesToIllegalRequest = new String[repliesToIllegalRequest.length];
		for (int i = 0; i < repliesToIllegalRequest.length; i++) {
			this.repliesToIllegalRequest[i] = repliesToIllegalRequest[i];
		}
	}

	/**
	 * Returns the name of the ChatterBot.
	 *
	 * @return The name of the bot.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Responds to a given statement by determining if it is a legal or illegal request.
	 * A legal request starts with the REQUEST_PREFIX, and the response will not include the prefix.
	 * An illegal and legal requests will result in a random reply from a predefined set of responses.
	 * In both cases, placeholders in the responses are replaced with relevant parts of the user's statement.
	 *
	 * @param statement The statement to respond to.
	 * @return A response based on the type of the request and the possible responses given when constructing
	 * the chatterBot.
	 */
	String replyTo(String statement) {
		if (statement.startsWith(REQUEST_PREFIX)) {
			return replyToLegalRequest(statement);
		}
		return replyToIllegalRequest(statement);
	}

	/**
	 * Creates a random response from the possible legal responses provided in the constructor.
	 * A legal request starts with the REQUEST_PREFIX, and the response will not include the prefix.
	 * placeholders in the response are replaced with relevant parts of the user's statement.
	 *
	 * @param statement The user's original statement.
	 * @return A response pattern with the placeholder replaced by the phrase.
	 */
	String replyToLegalRequest(String statement) {
		String phrase = statement.replaceFirst(REQUEST_PREFIX, "");
		// delete the request prefix to avoid replying with it TODO check forum
		return replacePlaceholderInARandomPattern(repliesToLegalRequest,
				PLACEHOLDER_FOR_REQUESTED_PHRASE, phrase);
	}

	/**
	 * Creates a random response from the possible illegal responses provided in the constructor.
	 * placeholders in the response are replaced with the user's statement.
	 *
	 * @param statement The user's original statement.
	 * @return A response pattern with the placeholder replaced by the statement.
	 */
	String replyToIllegalRequest(String statement) {
		return replacePlaceholderInARandomPattern(repliesToIllegalRequest,
				PLACEHOLDER_FOR_ILLEGAL_REQUEST, statement);
	}

	/**
	 * Chooses a random response pattern from an array and replaces a specified placeholder with a given
	 * statement in the pattern.
	 *
	 * @param possibleResponsePatterns An array of possible response patterns.
	 * @param placeHolder 			   The placeholder to be replaced in the response pattern.
	 * @param statementToPlace 		   The statement that replaces the placeholder.
	 * @return A response string with the placeholder replaced by the provided statement.
	 */
	String replacePlaceholderInARandomPattern(String[] possibleResponsePatterns,
											  String placeHolder, String statementToPlace) {
		int randomIndex = randomGenerator.nextInt(possibleResponsePatterns.length);
		String response_pattern = possibleResponsePatterns[randomIndex];
		return response_pattern.replaceAll(placeHolder, statementToPlace);
	}
}

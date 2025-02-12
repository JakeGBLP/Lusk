package it.jakegblp.lusk.api.enums;

/**
 * Represents a relation between two objects.
 */
public enum GenericRelation {

	EQUAL,
	NOT_EQUAL,
	GREATER,
	GREATER_OR_EQUAL,
	SMALLER,
	SMALLER_OR_EQUAL;

	/**
	 * @param b The boolean to get a Relation from.
	 * @return {@link #EQUAL} if 'b' is true else {@link #NOT_EQUAL}
	 */
	public static GenericRelation get(boolean b) {
		return b ? GenericRelation.EQUAL : GenericRelation.NOT_EQUAL;
	}

	/**
	 * @param i The int to get a Relation from.
	 * @return {@link #EQUAL} if 'i' is equal to 0,
	 * {@link #GREATER} if 'i' is greater than 0,
	 * {@link #SMALLER} if 'i' is less than 0
	 */
	public static GenericRelation get(int i) {
		return i == 0 ? GenericRelation.EQUAL : i > 0 ? GenericRelation.GREATER : GenericRelation.SMALLER;
	}

	/**
	 * @param d The double to get a Relation from.
	 * @return {@link #EQUAL} if 'd' is equal to 0,
	 * {@link #GREATER} if 'd' is greater than 0,
	 * {@link #SMALLER} if 'd' is less than 0
	 */
	public static GenericRelation get(double d) {
		return d == 0 ? GenericRelation.EQUAL : d > 0 ? GenericRelation.GREATER : GenericRelation.SMALLER;
	}
}

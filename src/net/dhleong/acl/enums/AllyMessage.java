package net.dhleong.acl.enums;

/**
 * Messages that can be sent to civilian NPCs.
 * @author rwalker 
 */
public enum AllyMessage implements CommsMessage {
	HAIL(0, "Hail."),
	TURN_TO_HEADING_0(1, "Turn to heading 0."),
	TURN_TO_HEADING_90(2, "Turn to heading 90."),
	TURN_TO_HEADING_180(3, "Turn to heading 180."),
	TURN_TO_HEADING_270(4, "Turn to heading 270."),
	TURN_LEFT_10_DEGREES(5, "Turn left 10 degrees."),
	TURN_RIGHT_10_DEGREES(6, "Turn right 10 degrees."),
	TURN_LEFT_25_DEGREES(15, "Turn left 25 degrees."),
	TURN_RIGHT_25_DEGREES(16, "Turn right 25 degrees."),
	ATTACK_NEAREST_ENEMY(7, "Attack nearest enemy."),
	PROCEED_TO_YOUR_DESTINATION(8, "Proceed to you destination."),
	GO_DEFEND(9, "Go defend:") {
		@Override
		public boolean hasArgument() {
			return true;
		}
	};

	/**
	 * Returns the AllyMessage that corresponds to the given ID.
	 */
	public static AllyMessage fromId(int id) {
		for (AllyMessage message : values()) {
			if (message.id == id) {
				return message;
			}
		}

		return null;
	}

	private int id;
	private String label;

	AllyMessage(int id, String label) {
		this.id = id;
		this.label = label;
	}

	@Override
	public String toString() {
		return label;
	}

	@Override
	public boolean hasArgument() {
		return false;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public CommsTargetType getTargetType() {
		return CommsTargetType.OTHER;
	}
}
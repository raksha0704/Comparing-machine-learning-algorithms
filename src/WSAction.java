public class WSAction extends Feature {

		enum Action {
			yes,
			no,
			Unknown
		}
		
		private Action action;
	
		public WSAction() {
			super("WSAction");
		}
		
		public WSAction(Action action) {
			super("WSAction");
			this.action = action;
		}

		public void setAction(Action action) {
			this.action = action;
		}

		public Action getAction() {
			return action;
		}
		
		public static Action determineAction(String action) {
			if(action.toLowerCase().contains("yes")) {
				return Action.yes;
			}
			else if(action.toLowerCase().contains("no")) {
				return Action.no;
			}
			else {
				return Action.Unknown;
			}
		}
}

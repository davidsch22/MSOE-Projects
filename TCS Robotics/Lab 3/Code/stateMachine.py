class StateTransition:
    """
    This class wraps a State Transition entity to store the required data
    """
    def __init__(self, from_state, to_state, condition):
        self.from_state = from_state
        self.to_state = to_state
        self.condition = condition


class StateMachine:
    """
    This class encompasses logic to create a FSM using provided action and condition 
    functions
    """
    def __init__(self):
        self.states = {}
        self.transitions = []
        self.default_state = None
        self.current_state = None

    def add_state(self, name, action_func, default=False):
        """
        This function adds a state to the state machine, with an associated function to
        execute when this state is active. The function should not loop, and should be 
        passed in without parenthesis. 

        Example:

        def my_func():
            pass

        add_state("MyFunc", my_func)
        """
        self.states[name] = action_func

        if default:
            self.default_state = name

        return self

    def add_transition(self, from_state, to_state, condition_func):
        """
        This function adds a transition between two states, with an associated function
        that is called to check whether the transition should occur. The condition should
        not loop, and should be passed in without parenthesis.

        Example:

        def my_condition():
            pass

        add_transition("From", "To", my_condition)
        """
        transition = StateTransition(from_state, to_state, condition_func)
        self.transitions.append(transition)

        return self

    def run(self):
        """
        This function is used as the primary execution loop for the defined FSM
        """
        # Confirm that both the default and current states have values
        if self.default_state is None:
            self.default_state = self.states.keys()[0]

        if self.current_state is None:
            self.current_state = self.default_state

        # Search through the transitions to see if any conditions are met
        for transition in self.transitions: 
            if transition.from_state == self.current_state:
                # If the stored condition is true, change states
                if transition.condition():
                    self.current_state = transition.to_state

        # Call the function stored for the state
        self.states[self.current_state]()

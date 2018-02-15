package Features.operations;

import Features.operations.actions.IAction;

/**
 * Created by Tobias on 14.02.2018.
 */

public class HumanConfirmableClaim extends StandardClaim {

    public HumanConfirmableClaim(IAction action, IActionProof proof) {
        super(action, proof);
    }

}

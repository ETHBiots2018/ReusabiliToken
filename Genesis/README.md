# Extended Genesis Library

This Folder contains an extended version of the Genesis library provided by the FuturICT2.0 challenge to add the functionality needed for ReusabiliToken.


# New Features

To get an unserstanding of what the changes are, consider looking at the following files:
```
Features.operations.actions.AHumanConfirmableAction
Features.operations.actions.BringOwnCupAction
Features.operations.actions.BringOwnPlateAction
Features.operations.HumanConfirmableActionProof
Features.operations.HumanConfirmableClaim
Features.operations.HumanConfirmableOperation
Features.tests.Address
Features.tests.DummyRepo
Features.tests.DummyValueToken
Features.tests.DummyReputationToken
Features.Utilities.StoreDatabase
```
TLDR: Addition of performable actions that are confirmable by a trusted human.

All dummy classes are just there to test the library. The store database is used to store public keys and ID's of the trusted instances able to confirm an action. This should later be stored on the blockchain to make a synchronized decentralized database avaliable to every instance participating in the program.

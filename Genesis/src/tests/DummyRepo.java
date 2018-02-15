package tests;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import Features.IFeature;
import Features.IOperation;
import Features.IUnderlying;
import Features.operations.IOperationProof;
import Features.operations.actions.IAction;
import Features.properties.IProperty;
import Repository.IRepository;
import Token.IToken;
import Utilities.IAddress;

/**
 * Created by Tobias on 15.02.2018.
 */


/**
 * This repo just contains mostly empty functions. Its functionality is not used by the actions except
 * for transferring money.
 */
public class DummyRepo implements IRepository {

    private Map<IAddress, Integer> val_token_amount = new HashMap<>();
    private Map<IAddress, Integer> rep_token_amount = new HashMap<>();

    @Override
    public long store(IToken token) {
        return 0;
    }

    @Override
    public IToken fetchToken(long uniqueID) {
        return null;
    }

    @Override
    public Collection getAllTokens() {
        return null;
    }

    @Override
    public Collection<IOperation> getOperations(IToken token) {
        return null;
    }

    @Override
    public Collection<IUnderlying> getUnderlyings(IToken token) {
        return null;
    }

    @Override
    public Collection<IProperty> getProperties(IToken token) {
        return null;
    }

    @Override
    public Collection<IFeature> getFeatures(IToken token) {
        return null;
    }

    @Override
    public int getCurrentTotalSupply(IToken token) {
        int sum = 0;
        if (token instanceof DummyValueToken)
            for (Integer v : val_token_amount.values())
                sum += v.intValue();
        else if (token instanceof DummyReputationToken)
            for (Integer v : rep_token_amount.values())
                sum += v.intValue();
        return sum;
    }

    @Override
    public int getBalanceOf(IToken token, IAddress addr) {
        if (token instanceof DummyValueToken)
            return val_token_amount.getOrDefault(addr, new Integer(0)).intValue();
        else if (token instanceof DummyReputationToken)
            return rep_token_amount.getOrDefault(addr, new Integer(0)).intValue();
        return 0;
    }

    @Override
    public Map<IAddress, Integer> getAllBalances(IToken token) {
        if (token instanceof DummyValueToken)
            return val_token_amount;
        else if (token instanceof DummyReputationToken)
            return rep_token_amount;
        return new HashMap<>();
    }

    @Override
    public boolean transfer(IToken token, IAddress from, IAddress to, int value) {
        if (token instanceof DummyValueToken) {
            if (val_token_amount.getOrDefault(from, new Integer(0)).intValue() < value)
                return false;
            val_token_amount.put(from, new Integer(val_token_amount.get(from).intValue() - value));
            val_token_amount.put(to, new Integer(val_token_amount.get(to).intValue() + value));
            return true;
        }
        return false;
    }

    @Override
    public boolean generate(IToken token, IAddress addr, int value) {
        if (token instanceof DummyValueToken)
            val_token_amount.put(addr, new Integer(val_token_amount.getOrDefault(addr, new Integer(0)).intValue() + value));
        else if (token instanceof DummyReputationToken)
            rep_token_amount.put(addr, new Integer(rep_token_amount.getOrDefault(addr, new Integer(0)).intValue() + value));
        return true;
    }

    @Override
    public boolean store(IAction action, IOperationProof proof) {
        return true;
    }

    @Override
    public boolean contains(IAction action) {
        return false;
    }

    @Override
    public IToken crateNewToken(Collection collection) {
        return null;
    }
}

package tests;

import java.util.Collection;
import java.util.Map;

import Features.operations.IClaim;
import Repository.IRepository;
import Token.IToken;
import Utilities.IAddress;

/**
 * Created by Tobias on 15.02.2018.
 */

/**
 * A dummy token for the value coin. It does not contain any functionality,
 * as it is not crucial for the proof of concept.
 */
public class DummyValueToken implements IToken {

    private IRepository repo;

    public DummyValueToken(IRepository repo) {
        this.repo = repo;
    }

    @Override
    public long getUniqueID() {
        return 0;
    }

    @Override
    public String getName() {
        return "DummyValueToken";
    }

    @Override
    public int getDecimals() {
        return 10;
    }

    @Override
    public String getSymbol() {
        return "DVT";
    }

    @Override
    public int getTotalSupply() {
        return -1;
    }

    @Override
    public boolean isCapped() {
        return false;
    }

    @Override
    public boolean preMined() {
        return false;
    }

    @Override
    public int preMinedAmount() {
        return 0;
    }

    @Override
    public int cappedAmount() {
        return 0;
    }

    @Override
    public int getBalanceOf(IAddress addr) {
        return repo.getBalanceOf(this, addr);
    }

    @Override
    public Map<IAddress, Integer> getAllBalances() {
        return repo.getAllBalances(this);
    }

    @Override
    public boolean transfer(IAddress from, IAddress to, int value) {
        return repo.transfer(this, from, to, value);
    }

    @Override
    public boolean generate(IAddress address, int value) {
        return repo.generate(this, address, value);
    }

    @Override
    public Collection getUnderlyings() {
        return null;
    }

    @Override
    public Collection getProperties() {
        return null;
    }

    @Override
    public Collection getOperations() {
        return null;
    }

    @Override
    public boolean addClaim(IClaim claim) {
        return false;
    }

}

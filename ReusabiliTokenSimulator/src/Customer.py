"""@package Customer
Implementation of a generic Customer
"""
from abc import ABCMeta, abstractmethod
import numpy as np


class Customer(object):
    """
    This is a generic Customer class
    """
    __metaclass__ = ABCMeta
    CUSTOMER_ID = 0
    def __init__(self, type_):
        self.customer_id = Customer.CUSTOMER_ID
        Customer.CUSTOMER_ID += 1
        self.reputation = {}
        self.coins = 0
        self.recycle_prob = 0.9
        self.preferred_shop = -1
        self.type_ = type_

    def transfer_coin(self, coin_count):
        """
        Transfer coins to the customer for book keeping
        :param coin_count: coin count
        :return: None
        """
        self.coins += coin_count

    def set_coin(self, coins):
        """
        Initialize coins for this customer
        :param coins: coin count
        :return: None
        """
        self.coins = coins

    def transfer_reputation(self, reputation, shop_address):
        """
        Transfer reputation to this customer from a specific shop
        :param reputation: reputation coins
        :param shop_address: the shop for which the reputation is being generated
        :return: None
        """
        if shop_address in self.reputation:
            self.reputation[shop_address]['reputation'] += reputation
        else:
            self.set_reputation(shop_address, reputation)

    def set_reputation(self, shop_address, reputation):
        """
        Initialize reputation for this customer
        :param shop_address: the shop for which the reputation is being initialized
        :param reputation: the reputation to give to the customer
        :return: None
        """
        self.reputation[shop_address] = {}
        self.reputation[shop_address]['reputation'] = reputation

    def get_address(self):
        """
        Get the address of this customer
        :return: customer address on the block chain
        """
        return self.customer_id

    def choose_to_recycle(self):
        """
        Takes a decision on whether this customer recycles at a given time instance
        :return: boolean flag indicating the customer's decision to recycle
        """
        res = np.random.binomial(1, self.recycle_prob, 1)
        if res == 1:
            return True
        else:
            return False

    def choose_to_pay_by_coin(self):
        """
        Takes a decision on whether this customer buys something with the coins he has
        at a given instance in time.
        :return: boolean flag indicating the customer's decision to buy with ReusabiliTokens
        """
        if self.coins > 300:
                return True
        else:
            return False

    def get_coin_spend(self):
        """
        Get the number of coins this customer spends every time he buys something with ReusabiliTokens
        :return: number of coins
        """
        return 300

    def get_coin(self):
        """
        Get the number of coins that this customer has.
        :return: None
        """
        return self.coins

    def get_reputation(self, shop_address):
        """
        Get the reputation that this customer has earned from a specific store
        :param shop_address: the shop address for which this customer's reputation is being queried
        :return: None
        """
        if shop_address in self.reputation:
            return self.reputation[shop_address]['reputation']
        else:
            return 0

    def get_type(self):
        """
        Get the type of the Customer. Currently implemented customers can either be Good, Bad or Neutral
        :return: None
        """
        return self.type_

    @abstractmethod
    def choose_shop(self, num_shops):
        """
        A choose shop strategy that depends on what type of a customer one is.
        :param num_shops: The total number of shops available
        :return: The chosen shop index
        """
        pass


class GoodCustomer(Customer):
    """
    A good customer recycles goods with a high probability and always revisits his favorite store
    to maximize his reputation.
    """
    def __init__(self):
        super(GoodCustomer, self).__init__('g')
        self.recycle_prob = 0.9

    def choose_shop(self, num_shops):
        if self.preferred_shop == -1:
            self.preferred_shop = np.random.randint(0, num_shops, 1)[0]

        return self.preferred_shop


class BadCustomer(Customer):
    """
    A bad customer recycles goods with a very low probability and always visits stores in an erratic
    fashion
    """
    def __init__(self):
        super(BadCustomer, self).__init__('b')
        self.recycle_prob = 0.1

    def choose_shop(self, num_shops):
        return np.random.randint(0, num_shops, 1)[0]


class NeutralCustomer(Customer):
    """
    A neutral customer recycles his goods with a decent probability and revisits a small subset of all stores
    that he likes to visit.
    """
    def __init__(self):
        super(NeutralCustomer, self).__init__('n')
        self.recycle_prob = 0.60
        self.preferred_shops = []
        self.preference_ratio = 0.30

    def choose_shop(self, num_shops):

        if len(self.preferred_shops) == 0:
            chosen_shops = np.random.choice(np.arange(num_shops), int(max(1, np.ceil(self.preference_ratio*num_shops))),
                                            replace=False)
            for shop in chosen_shops:
                self.preferred_shops.append(shop)

        # choose one of the preferred shops at random
        return np.random.choice(np.arange(len(self.preferred_shops)), 1, replace=False)[0]




"""@package Shop
Implementation of a shop
"""


class Shop(object):
    """
    This is an implementation of a generic shop that accepts ReusabiliTokens
    """
    SHOP_ID = 0
    def __init__(self):
        self.shop_id = Shop.SHOP_ID
        Shop.SHOP_ID += 1
        self.name = 'shop ' + str(self.shop_id)
        self.coin_count = 0
        self.cum_coin_count = 0

    def get_shop_address(self):
        """
        Get the address of this shop.
        :return: shop address
        """
        return self.shop_id

    def get_coin_count(self):
        """
        Get the number of coins that this shop is in possession of.
        :return: coin count of the shop
        """
        return self.coin_count

    def get_cum_coin_coint(self):
        """
        Get the cumulative number of coins collected at this shop
        :return: cumulative coin count
        """
        return self.cum_coin_count

    def buy_with_coins(self, coins):
        """
        Every time a customer decides to buy with ReusabiliTokens from this shop, the shop
        gets that many ReusabiliTokens
        :param coins: number of coins that this shop gets
        :return: None
        """
        self.coin_count += coins
        self.cum_coin_count += coins

    def pay_dues_to_smart_contract(self, smart_contract):
        """
        This function makes this shop pay whatever ReusabiliTokens it has to the smart contract.
        :param smart_contract: the smart contract to which the payment should be made
        :return: None
        """
        trans_res, payment = smart_contract.make_payment(self.shop_id, self.coin_count)
        if trans_res is True:
            self.coin_count -= payment


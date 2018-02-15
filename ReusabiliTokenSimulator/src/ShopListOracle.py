"""@package ShopListOracle
Implementation of a simple shop list oracle
"""


class ShopListOracle(object):
    """
    This is an implementation of an Oracle that checks if a specific address belongs
    to a registered shop.
    """
    def __init__(self):
        self.shop_list = []

    def verify_shop(self, shop_address):
        """
        The smart contract calls this function to verify if the given address is indeed a shop address
        :param shop_address: the address to verify
        :return: returns True if the given address is indeed that of a registered shop
        """
        if shop_address in self.shop_list:
            return True
        else:
            return False

    def register_new_shop(self, shop_address):
        """
        This function is used to register a new shop with the oracle.
        :param shop_address: the address of the shop to register
        :return: None
        """
        if shop_address not in self.shop_list:
            self.shop_list.append(shop_address)

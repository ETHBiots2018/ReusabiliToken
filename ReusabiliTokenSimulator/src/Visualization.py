"""@package Visualization
Implementation of a few visualizers
"""
import matplotlib.pyplot as plt
import numpy as np


def visualize_function(values, name, color, ax=None):
    """
    plot a function
    :param values: function values
    :param name: name of the function
    :param color: color of te plot
    :param ax: the axis on which to plot
    :return: plot axis
    """
    if ax is None:
        f = plt.figure()
        ax = f.add_subplot(1, 1, 1)
    ax.plot(values, c=color)
    ax.set_title(name)
    ax.grid(True)
    return ax


def visualize_market(smart_contract, customer_list, shop_list, ax_cus=None, ax_shop=None, ax_cp=None, ax_ca=None,
                     ax_shop_balances=None):
    """
    Function the visualizes the market
    :param smart_contract: the smart contract containing the state of the block chain
    :param customer_list: a list of customers in the block chain
    :param shop_list: a list of shops in the block chain
    :param ax_cus: the axis on which we plot the cumulative customer reputation
    :param ax_shop: the axis on which we plot the cumulative shop reputation
    :param ax_cp: the axis on which we plot the number of times a customer spent a coin
    :param ax_ca: the axis on which cumulative shop coin count
    :param ax_shop_balances: the axis on which live shop balances are plotted
    :return: plot axes
    """
    if ax_cus is None or ax_shop is None:
        f = plt.figure()
        ax_cus = f.add_subplot(3, 2, 1)
        ax_shop = f.add_subplot(3, 2, 2)
        ax_cp = f.add_subplot(3, 2, 3)
        ax_ca = f.add_subplot(3, 2, 4)
        ax_shop_balances = f.add_subplot(3, 2, 5)

    cus_reps = []
    shop_reps = []
    customer_colors = []
    for c in customer_list:
        if c.get_type() == 'g':
            customer_colors.append('green')
        if c.get_type() == 'b':
            customer_colors.append('red')
        if c.get_type() == 'n':
            customer_colors.append('yellow')

    ax_cus.set_title('customer reputation')
    ax_cus.grid(True)
    cus_labels = ['c' + str(i) for i in range(len(customer_list))]
    ax_cus.set_xticklabels(cus_labels)
    for xtick_, color in zip(ax_cus.get_xticklabels(), customer_colors):
        xtick_.set_color(color)
    ax_cus.set_xticks(np.arange(len(customer_list)))
    ax_cus.set_ylim(0, smart_contract.reputation_limit+100)
    ax_shop.set_title('shop reputation')
    ax_shop.grid(True)
    shop_labels = ['s' + str(i) for i in range(len(shop_list))]
    ax_shop.set_xticklabels(shop_labels)
    ax_shop.set_xticks(np.arange(len(shop_list)))
    ax_cp.set_title('coin purchases')
    ax_cp.grid(True)
    ax_cp.set_xticklabels(cus_labels)
    for xtick_, color in zip(ax_cp.get_xticklabels(), customer_colors):
        xtick_.set_color(color)
    ax_cp.set_xticks(np.arange(len(customer_list)))
    ax_ca.set_title('coins re-collected at shops')
    ax_ca.grid(True)
    ax_ca.set_xticklabels(shop_labels)
    ax_ca.set_xticks(np.arange(len(shop_list)))

    ax_shop_balances.set_xticks(np.arange(len(customer_list)))
    ax_shop_balances.set_title('live shop balance')
    ax_shop_balances.grid(True)
    ax_shop_balances.set_xticklabels(shop_labels)
    ax_shop_balances.set_xticks(np.arange(len(shop_list)))

    for customer in customer_list:
        cus_reps.append(smart_contract.calculate_customer_reputation(customer.get_address()))

    for shop in shop_list:
        shop_reps.append(smart_contract.calculate_shop_reputation(shop.get_shop_address()))

    cp_map = smart_contract.get_coin_purchase_map()
    coin_purchases = np.zeros(len(customer_list))
    for customer in cp_map:
        coin_purchases[customer] = cp_map[customer]

    coins_collected_in_shops = [shop.get_cum_coin_coint() for shop in shop_list]
    shop_balances = [shop.get_coin_count() for shop in shop_list]

    ax_cus.bar(np.arange(len(customer_list)), cus_reps, color=customer_colors)
    ax_shop.bar(np.arange(len(shop_list)), shop_reps, color='blue')
    ax_cp.bar(np.arange(len(customer_list)), coin_purchases, color=customer_colors)
    ax_ca.bar(np.arange(len(shop_list)), coins_collected_in_shops, color='magenta')
    ax_shop_balances.bar(np.arange(len(shop_list)), shop_balances, color='cyan')
    plt.tight_layout()
    return ax_cus, ax_shop, ax_cp, ax_ca, ax_shop_balances


def diminishing_returns(max_val, b1, num_samples=100):
    """
    Test function that generates values from a diminishing returns function
    :param max_val: function cap
    :param b1:0 >= some value <= 1
    :param num_samples: the number of values to generate
    :return: function values
    """
    x = np.arange(0, num_samples)
    return max_val - np.exp(np.log(max_val) - b1*x)


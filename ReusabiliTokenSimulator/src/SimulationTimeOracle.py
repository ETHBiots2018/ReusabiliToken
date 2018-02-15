"""@package SimulationTimeOracle
Implementation of a timing oracle
"""


class SimulationTimeOracle(object):
    """
    An oracle that gives the True time
    """
    def __init__(self):
        self.time = 0

    def increment_time(self):
        """
        Increment the wall clock time on the Oracle
        :return:
        """
        self.time += 1

    def get_time(self):
        """
        A smart contract would call this function on the oracle to determine some standard wall clock
        time
        :return: False
        """
        return self.time


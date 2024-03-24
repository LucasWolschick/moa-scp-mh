from dataclasses import dataclass


@dataclass
class Entry:
    best: float
    avg: float
    stddev: float
    time: float


series = [
    Entry(79229.0, 84659.85, 2092.813756864409, 2.742),
    Entry(72514.0, 81213.741, 4014.4101858790223, 9.693),
    Entry(69356.0, 78955.56566666666, 4624.899510062961, 16.766),
    Entry(69356.0, 77495.01775, 4765.870258647995, 24.293),
    Entry(69111.0, 76340.2458, 4864.0068532760215, 33.013),
    Entry(69111.0, 75511.68183333334, 4824.231613433029, 41.442),
    Entry(68703.0, 74901.86557142857, 4724.513551691294, 49.98),
    Entry(68364.0, 74437.0485, 4602.807414112519, 58.41),
    Entry(68364.0, 74070.24577777777, 4477.520774647841, 67.03),
    Entry(68364.0, 73779.1294, 4352.781611203475, 75.894),
]

series_search = [
    Entry(79967.0, 84379.66, 2114.155600573038, 0.18),
    Entry(61461.0, 75114.37, 10485.656959027987, 1635.539),
    Entry(60513.0, 70855.26666666666, 10473.488128962286, 2354.276),
    Entry(60204.0, 68623.73, 9862.672046247384, 2851.452),
    Entry(60204.0, 67248.502, 9242.748986294244, 3273.405),
    Entry(59878.0, 66298.29166666667, 8703.835732251648, 3631.772),
    Entry(59878.0, 65619.22, 8230.830063126603, 3963.155),
    Entry(59878.0, 65083.13, 7831.610843952469, 4295.845),
    Entry(59878.0, 64673.36777777778, 7477.632997629426, 4608.296),
    Entry(59878.0, 64344.88, 7165.58409154292, 4903.767),
]

import matplotlib.pyplot as plt


def plot_best_comparative(series1, series2):
    plt.plot(
        range(1, len(series1) + 1),
        [entry.best for entry in series1],
        label="sem busca local",
    )
    plt.plot(
        range(1, len(series2) + 1),
        [entry.best for entry in series2],
        label="com busca local",
    )
    plt.xlabel("Geração")
    plt.ylabel("Custo")
    plt.grid(axis="x", linestyle="--")
    # only integer steps in x axis
    plt.xticks(range(1, max(len(series1), len(series2)) + 1))
    plt.legend()
    plt.show()


def plot_series_avg_stddev(series):
    plt.plot(
        range(1, len(series_search) + 1),
        [entry.best for entry in series_search],
        label="melhor",
    )
    plt.plot(
        range(1, len(series_search) + 1),
        [entry.avg for entry in series_search],
        label="média",
    )
    plt.xlabel("Geração")
    plt.ylabel("Custo")
    plt.grid(axis="x", linestyle="--")
    # only integer steps in x axis
    plt.xticks(range(1, len(series_search) + 1))
    lines, labels = plt.gca().get_legend_handles_labels()

    # stddev on right side y axis
    ax2 = plt.twinx()
    plt.ylabel("Desvio padrão")
    plt.plot(
        range(1, len(series_search) + 1),
        [entry.stddev for entry in series_search],
        color="red",
        label="desvio padrão",
    )
    lines2, labels2 = plt.gca().get_legend_handles_labels()

    # common legend for both plots
    plt.legend(lines + lines2, labels + labels2, loc="upper right")

    plt.show()


plot_best_comparative(series, series_search)

const API_BASE = "/api";

let trendingChart = null;
let languageChart = null;
let commitChart = null;

async function fetchData(endpoint) {

    const response = await fetch(API_BASE + endpoint);

    if (!response.ok) {
        throw new Error(
            `API request failed: ${response.status}`
        );
    }

    return await response.json();
}

async function loadDashboard() {

    const status =
        document.getElementById("dashboardStatus");

    status.textContent = "Loading dashboard...";

    try {

        const [
            repositories,
            trending,
            languages
        ] = await Promise.all([

            fetchData("/repositories"),
            fetchData("/trending"),
            fetchData("/languages")

        ]);

        updateSummary(repositories, trending, languages);

        displayTrending(trending);
        createTrendingChart(trending);

        displayLanguages(languages);
        createLanguageChart(languages);

        displayRepositories(repositories);

        status.textContent = "Dashboard updated successfully.";

    } catch (error) {

        console.error("Dashboard error:", error);

        status.textContent =
            "Failed to load dashboard. Check the Spring Boot console.";
    }
}

function updateSummary(repositories, trending, languages) {

    document.getElementById("totalRepositories")
        .textContent = repositories.length;

    document.getElementById("totalLanguages")
        .textContent = languages.length;

    document.getElementById("totalTrending")
        .textContent = trending.length;
}

function displayTrending(trending) {

    const table =
        document.getElementById("trendingTable");

    table.innerHTML = "";

    if (trending.length === 0) {

        const row = table.insertRow();
        const cell = row.insertCell();

        cell.colSpan = 4;
        cell.textContent =
            "No trending statistics available yet.";

        return;
    }

    trending.forEach(repo => {

        const row = table.insertRow();

        row.insertCell().textContent = repo.rank;

        const nameCell = row.insertCell();

        const link = document.createElement("a");

        link.textContent = repo.fullName ?? "Unknown";

        if (repo.url &&
            repo.url.startsWith("https://github.com/")) {

            link.href = repo.url;
            link.target = "_blank";
            link.rel = "noopener noreferrer";

        }

        nameCell.appendChild(link);

        row.insertCell().textContent =
            repo.primaryLanguage ?? "Unknown";

        row.insertCell().textContent =
            repo.starGrowth ?? 0;
    });
}

function displayLanguages(languages) {

    const table =
        document.getElementById("languageTable");

    table.innerHTML = "";

    if (languages.length === 0) {

        const row = table.insertRow();
        const cell = row.insertCell();

        cell.colSpan = 3;
        cell.textContent =
            "No language statistics available.";

        return;
    }

    languages.forEach(language => {

        const row = table.insertRow();

        row.insertCell().textContent =
            language.language;

        row.insertCell().textContent =
            language.repositoryCount;

        row.insertCell().textContent =
            Number(language.percentage).toFixed(2) + "%";
    });
}

function displayRepositories(repositories) {

    const table =
        document.getElementById("repositoryTable");

    table.innerHTML = "";

    if (repositories.length === 0) {

        const row = table.insertRow();
        const cell = row.insertCell();

        cell.colSpan = 3;
        cell.textContent =
            "No repositories have been collected.";

        return;
    }

    repositories.forEach(repo => {

        const row = table.insertRow();

        row.insertCell().textContent =
            repo.fullName ?? "Unknown";

        row.insertCell().textContent =
            repo.primaryLanguage ?? "Unknown";

        const buttonCell = row.insertCell();

        const button = document.createElement("button");

        button.textContent = "View Commits";

        button.addEventListener("click", () => {
            loadCommitActivity(repo.id, repo.fullName);
        });

        buttonCell.appendChild(button);
    });
}

async function loadCommitActivity(repositoryId, repositoryName) {

    const container =
        document.getElementById("commitActivity");

    const heading =
        document.getElementById("selectedRepository");

    heading.textContent =
        "Weekly Commit Activity: " + repositoryName;

    container.textContent = "Loading commit activity...";

    try {

        const commits = await fetchData(
            "/commits/" + encodeURIComponent(repositoryId)
        );

        container.innerHTML = "";

        if (commits.length === 0) {

            container.textContent =
                "No saved commit activity for this repository.";

            return;
        }

        createCommitChart(commits, repositoryName);

        const table = document.createElement("table");

        const header = table.createTHead();
        const headerRow = header.insertRow();

        ["Week Starting", "Total Commits"].forEach(text => {

            const cell = document.createElement("th");
            cell.textContent = text;
            headerRow.appendChild(cell);
        });

        const body = table.createTBody();

        commits.forEach(week => {

            const row = body.insertRow();

            const date = new Date(week.week * 1000);

            row.insertCell().textContent =
                date.toLocaleDateString("en-US", {
                    timeZone: "UTC",
                    year: "numeric",
                    month: "short",
                    day: "numeric"
                });

            row.insertCell().textContent =
                week.total;
        });

        container.appendChild(table);

    } catch (error) {

        console.error("Commit activity error:", error);

        container.textContent =
            "Unable to load commit activity.";
    }
}

function createTrendingChart(trending) {

    const canvas =
        document.getElementById("trendingChart");

    if (trendingChart !== null) {
        trendingChart.destroy();
    }

    const labels = trending.map(repo => repo.fullName);

    const values = trending.map(repo => repo.starGrowth);

    trendingChart = new Chart(canvas, {

        type: "bar",

        data: {

            labels: labels,

            datasets: [{
                label: "Star Growth",
                data: values,
                backgroundColor: "rgba(9, 105, 218, 0.7)",
                borderColor: "rgba(9, 105, 218, 1)",
                borderWidth: 1
            }]
        },

        options: {

            responsive: true,
            maintainAspectRatio: false,

            plugins: {
                title: {
                    display: true,
                    text: "Repository Star Growth"
                }
            },

            scales: {
                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: "Stars Gained"
                    }
                }
            }
        }
    });
}

function createLanguageChart(languages) {

    const canvas =
        document.getElementById("languageChart");

    if (languageChart !== null) {
        languageChart.destroy();
    }

    const labels =
        languages.map(language => language.language);

    const values =
        languages.map(language => language.repositoryCount);

    languageChart = new Chart(canvas, {

        type: "doughnut",

        data: {

            labels: labels,

            datasets: [{
                label: "Repositories",
                data: values,

                backgroundColor: [
                    "#0969da",
                    "#2da44e",
                    "#bf8700",
                    "#8250df",
                    "#cf222e",
                    "#1b7c83",
                    "#d29922",
                    "#57606a",
                    "#dd7815",
                    "#6f42c1"
                ],

                borderWidth: 1
            }]
        },

        options: {

            responsive: true,
            maintainAspectRatio: false,

            plugins: {

                title: {
                    display: true,
                    text: "Programming Language Distribution"
                },

                legend: {
                    position: "bottom"
                },

                tooltip: {
                    callbacks: {
                        label: function(context) {

                            const total = context.dataset.data
                                .reduce((sum, value) => sum + value, 0);

                            const count = context.parsed;

                            const percentage = total > 0
                                ? (count / total * 100).toFixed(2)
                                : "0.00";

                            return `${context.label}: ${count} repositories (${percentage}%)`;
                        }
                    }
                }
            }
        }
    });
}

function createCommitChart(commits, repositoryName) {

    const canvas =
        document.getElementById("commitChart");

    if (commitChart !== null) {
        commitChart.destroy();
    }

    // Ensure weeks are displayed chronologically.
    const sortedCommits = [...commits]
        .sort((a, b) => a.week - b.week);

    const labels = sortedCommits.map(week => {

        const date = new Date(week.week * 1000);

        return date.toLocaleDateString("en-US", {
            timeZone: "UTC",
            month: "short",
            day: "numeric",
            year: "numeric"
        });
    });

    const values =
        sortedCommits.map(week => week.total);

    commitChart = new Chart(canvas, {

        type: "line",

        data: {

            labels: labels,

            datasets: [{
                label: "Weekly Commits",
                data: values,

                borderColor: "#2da44e",
                backgroundColor: "rgba(45, 164, 78, 0.15)",

                borderWidth: 2,
                tension: 0.2,
                fill: true
            }]
        },

        options: {

            responsive: true,
            maintainAspectRatio: false,

            plugins: {
                title: {
                    display: true,
                    text: "Weekly Commit Activity: " + repositoryName
                }
            },

            scales: {
                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: "Number of Commits"
                    }
                },

                x: {
                    title: {
                        display: true,
                        text: "Week Starting"
                    }
                }
            }
        }
    });
}

document.getElementById("refreshButton")
    .addEventListener("click", loadDashboard);

loadDashboard();
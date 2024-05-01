google.charts.load("current", {packages: ["corechart"]});
google.charts.setOnLoadCallback(drawGenre);
google.charts.setOnLoadCallback(drawCategory);

function drawMileage() {
    let res = [['Транспорт', 'Пробег']];

    for (let i = 0; i < mileageString.length; i++) {
        res.push([mileageString[i], mileageInt[i]]);
    }

    var data = google.visualization.arrayToDataTable(res);

    let options = {
        title: 'По пробегу',
        hAxis: {title: 'Транспорт'},
        vAxis: {title: 'Пробег'},
        bar: {groupWidth: "80%"},
        legend: {position: "none"}
    };

    let chart = new google.visualization.ColumnChart(document.getElementById('drawMileage'));
    chart.draw(data, options);
}

function drawGenre() {
    let res = [['Категория', 'Количество']];

    for (let i = 0; i < genresString.length; i++) {
        res.push([genresString[i], genresInt[i]]);
    }

    var data = google.visualization.arrayToDataTable(res);

    var options = {
        title: 'Жанры по количеству',
        pieHole: 0.2,

    };

    var chart = new google.visualization.PieChart(document.getElementById('drawGenre'));
    chart.draw(data, options);
}

function drawCategory() {
    let res = [['Жанр', 'Количество']];

    for (let i = 0; i < categoriesString.length; i++) {
        res.push([categoriesString[i], categoriesInt[i]]);
    }

    var data = google.visualization.arrayToDataTable(res);

    var options = {
        title: 'Категории по количеству',
        is3D: true,
    };

    var chart = new google.visualization.PieChart(document.getElementById('drawCategory'));
    chart.draw(data, options);
}
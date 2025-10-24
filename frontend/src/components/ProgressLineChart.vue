<template>
  <div class="container container--wide">
    <h2></h2>

    <div v-if="isLoading" class="message">
      Laen graafiku andmeid...
    </div>

    <div v-if="error" class="message message--error">
      Viga andmete laadimisel: {{ error }}
    </div>

    <div v-if="!isLoading && !error" class="chart-container">
      <canvas ref="chartCanvas"></canvas>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue';
import axios from 'axios';
import Chart from 'chart.js/auto';
import 'chartjs-adapter-date-fns';
import { et } from 'date-fns/locale';
import {useRoute} from "vue-router";

const chartCanvas = ref(null);
let chartInstance = null;
const isLoading = ref(true);
const error = ref(null);

const route = useRoute()
const courseId = route.params.courseId

async function fetchAndRenderChart() {
  isLoading.value = true;
  error.value = null;
  try {
    const response = await axios.get(`/api/courses/${courseId}/grades/progress-lines`);
    isLoading.value = false;
    await nextTick();
    renderChart(response.data);
  } catch (err) {
    error.value = "Andmete toomine ebaõnnestus.";
    isLoading.value = false;
  }
}

// Draw graph
function renderChart(apiData) {
  console.log("3. Alustan graafiku joonistamist nende andmetega:", apiData);

  if (!chartCanvas.value) {
    console.error("VIGA: Canvas element ei ole leitav! 'ref' ei tööta.");
    return;
  }

  // No data handeled
  if (!apiData || apiData.length === 0) {
    console.warn("Andmed on tühjad, graafikut ei joonistata.");
    error.value = "Sellel kursusel ei leitud tudengite andmeid.";
    return;
  }

  const datasets = apiData.map((student, index) => {
    const colors = ['#e4067e', '#4dbed2', '#342b60', '#f2b700', '#9396b0'];
    const color = colors[index % colors.length];

    if (!student.dataPoints || !Array.isArray(student.dataPoints)) {
      console.error("VIGA: Tudengi objektil puuduvad või on vales formaadis 'dataPoints'", student);
      return null;
    }

    return {
      label: `${student.firstName} ${student.lastName}`,
      data: student.dataPoints,
      borderColor: color,
      backgroundColor: `${color}33`,
      fill: false,
      tension: 0.1
    };
  }).filter(Boolean); //null removed

  const chartData = { datasets };
  const chartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        display: false,
      },
      title: {
        display: true,
        text: 'Tudengite õpikõverad',
        font: {
          size: 18
        }
      },
      // Index shows values on hover
      tooltip: {
        mode: 'index',
        intersect: false,
      }
    },

    // 'scales' controls X- and Y-axis conf
    scales: {
      x: {
        type: 'time',
        time: {
          unit: 'day',
          // Hover format
          tooltipFormat: 'dd.MM.yyyy HH:mm',
          // Axis format
          displayFormats: {
            day: 'dd.MM.yyyy'
          }
        },

        adapters: {
          date: {
            locale: et
          }
        },

        title: {
          display: true,
          text: 'Kuupäev'
        }
      },

      y: {
        beginAtZero: true,

        title: {
          display: true,
          text: 'Kumulatiivne punktisumma'
        }
      }
    }
  };

  try {
    if (chartInstance) {
      chartInstance.destroy();
    }

    chartInstance = new Chart(chartCanvas.value, {
      type: 'line',
      data: chartData,
      options: chartOptions,
    });
    console.log("6. Graafik on edukalt loodud!");
  } catch(e) {
    console.error("KRIITILINE VIGA: Chart.js viskas vea graafiku loomisel!", e);
    error.value = "Graafiku joonistamine ebaõnnestus.";
  }
}

onMounted(() => {
  fetchAndRenderChart();
});
</script>

<style scoped>
.chart-container {
  height: 500px;
  width: 100%;
  position: relative;
}

.message {
  padding: 2rem 0;
}
</style>

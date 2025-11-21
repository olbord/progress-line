<template>
  <div class="container container--wide">

    <div v-if="isLoading" class="message">
      Laen graafiku andmeid...
    </div>

    <div v-if="error" class="message message--error">
      Viga andmete laadimisel: {{ error }}
    </div>

    <div v-if="!isLoading && !error" class="container--wide">
      <canvas ref="chartCanvas"></canvas>
    </div>
  </div>
</template>

<script setup>
import { useRouter, useRoute } from 'vue-router';
import { ref, onMounted, nextTick } from 'vue';
import Chart from 'chart.js/auto';
import 'chartjs-adapter-date-fns';
import { et } from 'date-fns/locale';
import apiClient from "@/services/api.js";
import {getToken} from "@/msal/msalInstance.js";

const router = useRouter();
let apiStudentData = [];
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
    const token = await getToken();
    const response = await apiClient.get(
        `/courses/${courseId}/progress-lines`,
        { headers: { Authorization: `Bearer ${token}` } }
    );

    apiStudentData = response.data;
    isLoading.value = false;
    await nextTick();
    renderChart(apiStudentData);

  } catch (err) {
    console.error("Viga andmete laadimisel:", err);
    error.value = "Andmete laadimine ebaõnnestus. Proovi uuesti!";
    isLoading.value = false;
  }
}

// Draw graph
function renderChart(apiData) {

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

  const datasets = apiData.map((student) => {
    return {
      label: `${student.firstName} ${student.lastName}`,
      data: student.dataPoints,
      borderColor: '#342b60',
      borderWidth: 2,
      fill: false,
      tension: 0.1,
    };
  });

  const chartData = { datasets };
  let currentlyHoveredIndex = null;

  const chartOptions = {
    responsive: true,
    maintainAspectRatio: false,

    // Find nearest line to cursor
    interaction: {
      mode: 'dataset',
      intersect: false,
    },

    // Change cursor to hand when hovering
    onHover: (event, activeElements, chart) => {
      event.native.target.style.cursor = activeElements.length ? 'pointer' : 'default';

      // What line is active
      const newHoveredIndex = activeElements.length > 0 ? activeElements[0].datasetIndex : null;

      // Do nothing if same line
      if (newHoveredIndex === currentlyHoveredIndex) {
        return;
      }

      // Recolor lines if new line is hovered
      currentlyHoveredIndex = newHoveredIndex;

      for (const [index, dataset] of chart.data.datasets.entries()) {
        if (index === currentlyHoveredIndex) {
          // Make active line thich and pink
          dataset.borderColor = '#e4067e';
          dataset.borderWidth = 4;
        } else {
          dataset.borderColor = '#342b60';
          dataset.borderWidth = 2;
        }
      }

      chart.update('none');
    },

    // Clicking effect
    onClick: (event, elements) => {
      if (elements.length === 0) return;

      const firstElement = elements[0];
      const datasetIndex = firstElement.datasetIndex;

      // Find the student who's line was clicked
      const clickedStudent = apiStudentData[datasetIndex];
      console.log("Klikiti tudengi andmetel: ", clickedStudent)

      if (clickedStudent && clickedStudent.studentId) {
        // Go to create a comment
        router.push(`/courses/${courseId}/students/${clickedStudent.studentId}/grades`);
        console.log("Tudengi id on ", clickedStudent.studentId)
      } else {
        console.error("Ei saanud tudengi ID-d kätte. Tudeng: ", clickedStudent)
      }
    },

    plugins: {
      legend: {
        display: false,
      },
      title: {
        display: true,
        text: 'Tudengite õpikõverad',
        font: { size: 18 }
      },

      tooltip: {
        enabled: true,
        mode: 'nearest',
        intersect: false,
        displayColors: false,

        filter: function(tooltipItem) {
          // 'tooltipItem' contains data for one line
          // '_active' is Chart.js inner variable to hold active elements
          const activeElements = tooltipItem.chart._active || [];
          if (activeElements.length > 0) {
            // Show only this tooltip line, that matches hovered lines datasetIndex
            return tooltipItem.datasetIndex === activeElements[0].datasetIndex;
          }
          return false;
        },

        callbacks: {
          label: function(context) {
            const label = context.dataset.label || '';
            const value = context.parsed.y;
            return `${label}: ${value} punkti`;
          },

          footer: function(tooltipItems) {
            return '\n'+'Kliki, et vaadata hindeid';
          }
        }
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
    console.error("KRIITILINE VIGA: Chart.js viga graafiku loomisel!", e);
    error.value = "Graafiku joonistamine ebaõnnestus.";
  }
}

onMounted(() => {
  fetchAndRenderChart();
});
</script>


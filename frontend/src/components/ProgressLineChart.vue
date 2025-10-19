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

// --- 1. Reaktiivsed muutujad ---
const chartCanvas = ref(null); // See seotakse <canvas> elemendiga template'is
let chartInstance = null; // Hoiame siin viidet graafikule, et seda hiljem uuendada
const isLoading = ref(true); // Alguses on laadimise seisund tõene
const error = ref(null);     // Vigade hoidmiseks

// TODO: get courseId from path/login data
const courseId = 35065;

// --- 2. Andmete toomise funktsioon ---
async function fetchAndRenderChart() {
  isLoading.value = true;
  error.value = null;
  try {
    const response = await axios.get(`/api/grades/courses/${courseId}/grades/progress-lines`);
    isLoading.value = false;
    await nextTick();
    renderChart(response.data);
  } catch (err) {
    error.value = "Andmete toomine ebaõnnestus.";
    isLoading.value = false;
  }
}

// --- 3. Graafiku joonistamise funktsioon ---
function renderChart(apiData) {
  // --- KONTROLLPUNKT 1: Kas funktsioon käivitub ja mis andmed sisse tulevad? ---
  console.log("3. Alustan graafiku joonistamist nende andmetega:", apiData);

  if (!chartCanvas.value) {
    console.error("VIGA: Canvas element ei ole leitav! 'ref' ei tööta.");
    return;
  }
  // --- KONTROLLPUNKT 2: Kas canvas element on olemas? ---
  console.log("Canvas element leitud:", chartCanvas.value);


  // Lisame kaitse tühjade andmete vastu, mis võib ka olla probleem
  if (!apiData || apiData.length === 0) {
    console.warn("Andmed on tühjad, graafikut ei joonistata.");
    error.value = "Sellel kursusel ei leitud tudengite andmeid.";
    return;
  }

  const datasets = apiData.map((student, index) => {
    // ... (värvide loogika jääb samaks)
    const colors = ['#e4067e', '#4dbed2', '#342b60', '#f2b700', '#9396b0'];
    const color = colors[index % colors.length];

    // Veendu, et andmepunktid on olemas ja on massiiv
    if (!student.dataPoints || !Array.isArray(student.dataPoints)) {
      console.error("VIGA: Tudengi objektil puuduvad või on vales formaadis 'dataPoints'", student);
      return null; // Tagastame nulli, et vältida edasisi vigu
    }

    return {
      label: `${student.firstName} ${student.lastName}`,
      data: student.dataPoints,
      borderColor: color,
      backgroundColor: `${color}33`,
      fill: false,
      tension: 0.1
    };
  }).filter(Boolean); // Eemaldame kõik 'null' väärtused, kui mõnel tudengil olid vigased andmed

  // --- KONTROLLPUNKT 3: Kuidas näevad välja Chart.js jaoks teisendatud andmed? ---
  console.log("4. Teisendasin andmed 'datasets' formaati:", datasets);

  // ... (chartData ja chartOptions jäävad samaks)
  const chartData = { datasets };
  const chartOptions = {
    // Teeb graafiku responsiivseks, et see kohanduks konteineri suurusega
    responsive: true,

    // Oluline! See luba graafikul täita konteineri kõrgus ja laius,
    // ignoreerides Chart.js'i vaikimisi kuvasuhet.
    maintainAspectRatio: false,

    // Pluginad on lisamoodulid, mis kontrollivad nt pealkirju ja legende
    plugins: {
      legend: {
        display: false,
      },
      title: {
        display: true,
        text: 'Tudengite õpikõverad', // Graafiku pealkiri
        font: {
          size: 18 // Suurem fondi suurus pealkirjale
        }
      },
      // Index shows values on hover
      tooltip: {
        mode: 'index',
        intersect: false,
      }
    },

    // 'scales' objekt kontrollib X- ja Y-telgede seadeid
    scales: {
      // X-telje (horisontaalne) konfiguratsioon
      x: {
        // KÕIGE OLULISEM: anname teada, et see on ajatelg
        type: 'time',

        // Seaded, mis on spetsiifilised ajateljele
        time: {
          // Mis ühikutes kuvatakse telje jaotusi (nt 'day', 'week', 'month')
          unit: 'day',

          // Kuidas kuvatakse kuupäeva formaati tooltip'is (kui hiirega peale minna)
          tooltipFormat: 'dd.MM.yyyy HH:mm',

          // Kuidas kuvatakse kuupäeva formaati telje peal
          displayFormats: {
            day: 'dd.MM.yyyy'
          }
        },

        // See on see osa, mis paneb Eesti lokaadi tööle!
        adapters: {
          date: {
            locale: et // Siin kasutame imporditud 'et' muutujat
          }
        },

        // Telje enda pealkiri
        title: {
          display: true,
          text: 'Kuupäev'
        }
      },

      // Y-telje (vertikaalne) konfiguratsioon
      y: {
        // Tagab, et telg algab alati nullist
        beginAtZero: true,

        // Telje pealkiri
        title: {
          display: true,
          text: 'Kumulatiivne punktisumma'
        }
      }
    }
  };

  try {
    // --- KONTROLLPUNKT 4: Proovin luua graafikut ---
    console.log("5. Loon uue Chart objekti.");

    // Kui graafik on juba olemas, hävitame vana enne uue joonistamist
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

// --- 4. Elutsükli konks ---
// See funktsioon käivitub automaatselt, kui komponent lehele laetakse
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

  
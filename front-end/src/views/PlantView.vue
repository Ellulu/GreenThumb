<template>
    <div class="container mx-auto px-4 py-8">
      <h1 class="text-3xl font-bold mb-6 text-green-700">Mes Plantes</h1>
      
      <div v-if="loading" class="text-center py-8">
        <Loader2Icon class="animate-spin h-8 w-8 mx-auto text-green-600" />
        <p class="mt-2 text-gray-600">Chargement des plantes...</p>
      </div>
  
      <div v-else-if="error" class="bg-red-100 border-l-4 border-red-500 text-red-700 p-4 mb-6" role="alert">
        <p>{{ error }}</p>
      </div>
  
      <div v-else-if="plants.length === 0" class="text-center py-8">
        <LeafIcon class="h-16 w-16 mx-auto text-gray-400" />
        <p class="mt-4 text-xl text-gray-600">Vous n'avez pas encore de plantes.</p>
        <button @click="showAddPlantModal" class="mt-4 bg-green-600 hover:bg-green-700 text-white font-bold py-2 px-4 rounded transition duration-300">
          Ajouter une plante
        </button>
      </div>
  
      <div v-else class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
        <div v-for="plant in plants" :key="plant.id" class="bg-white rounded-lg shadow-md overflow-hidden transition duration-300 hover:shadow-lg">
            <img :src="'https://pbs.twimg.com/media/Bo6JgO-IIAA26OQ.jpg'" :alt="plant.name" class="w-full h-48 object-cover">
            <div class="p-4">
            <h2 class="text-xl font-semibold text-gray-800 mb-2">{{ plant.name }}</h2>
            <p class="text-gray-600 mb-2">Variété: {{ plant.variety.varietyName }}</p>
            <div class="flex items-center mb-2">
              <SunIcon class="h-5 w-5 text-yellow-500 mr-1" />
              <div class="bg-gray-200 w-full rounded-full h-2">
                <div class="bg-yellow-500 rounded-full h-2" :style="{ width: `${plant.luminosity}%` }"></div>
              </div>
            </div>
            <div class="flex items-center mb-4">
              <DropletIcon class="h-5 w-5 text-blue-500 mr-1" />
              <div class="bg-gray-200 w-full rounded-full h-2">
                <div class="bg-blue-500 rounded-full h-2" :style="{ width: `${plant.watering}%` }"></div>
              </div>
            </div>
            <div class="flex justify-between">
              <button @click="editPlant(plant)" class="text-green-600 hover:text-green-800 transition duration-300">
                <EditIcon class="h-5 w-5" />
              </button>
              <button @click="deletePlant(plant.id)" class="text-red-600 hover:text-red-800 transition duration-300">
                <TrashIcon class="h-5 w-5" />
              </button>
            </div>
          </div>
        </div>
      </div>
  
      <button @click="showAddPlantModal" class="fixed bottom-6 right-6 bg-green-600 hover:bg-green-700 text-white font-bold py-3 px-6 rounded-full shadow-lg transition duration-300">
        <PlusIcon class="h-6 w-6" />
      </button>
  
    </div>
  </template>
  
  <script setup>
  import { onMounted, ref } from 'vue';
  import { usePlantStore } from '@/stores/usePlantStore'
  import { SunIcon, DropletIcon, EditIcon, TrashIcon, PlusIcon, LeafIcon, Loader2Icon } from 'lucide-vue-next';
  
  const plantStore = usePlantStore();
  const loading = ref(true);
  const error = ref(null);
  
  onMounted(async () => {
    try {
      await plantStore.fetchPlants();
      loading.value = false;
    } catch (err) {
      error.value = "Erreur lors du chargement des plantes.";
      loading.value = false;
    }
  });
  
  const { plants } = plantStore;
  
  const showAddPlantModal = () => {
    console.log("Afficher le modal d'ajout de plante");
  };
  
  const editPlant = (plant) => {
    console.log("Éditer la plante:", plant);
  };
  
  const deletePlant = async (id) => {
    if (confirm("Êtes-vous sûr de vouloir supprimer cette plante ?")) {
      try {
        await plantStore.deletePlant(id);
        await plantStore.fetchPlants();
      } catch (err) {
        error.value = "Erreur lors de la suppression de la plante.";
      }
    }
  };
  </script>
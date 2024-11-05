<template>
  <div class="container mx-auto px-4 py-8">
    <Title_2>Mes Plantes</Title_2>
    
    <div v-if="loading" class="text-center py-8">
      <Loader2Icon class="animate-spin h-8 w-8 mx-auto text-green-600" />
      <p class="mt-2 text-gray-600">Chargement des plantes...</p>
    </div>

    <div v-else-if="error" class="bg-red-100 border-l-4 border-red-500 text-red-700 p-4 mb-6" role="alert">
      <p>{{ error }}</p>
    </div>

    <div v-else-if="plantStore.plants.length === 0" class="text-center py-8">
      <LeafIcon class="h-16 w-16 mx-auto text-gray-400" />
      <p class="mt-4 text-xl text-gray-600">Vous n'avez pas encore de plantes.</p>
      <button @click="showAddPlantModal" class="mt-4 bg-green-600 hover:bg-green-700 text-white font-bold py-2 px-4 rounded transition duration-300">
        Ajouter une plante
      </button>
    </div>

    <div v-else class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
      <div v-for="plant in plantStore.plants" :key="plant.id" class="bg-white rounded-lg shadow-md overflow-hidden transition duration-300 hover:shadow-lg">
        <img :src="'https://www.thespruce.com/thmb/npVjfgrsic53EqOwpgthAVMAnBo=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/how-to-grow-monstera-deliciosa-5072671-01-a65286b8b3b8402882c7ad2c57756bbe.jpg'" :alt="plant.name" class="w-full h-48 object-cover">
        <div class="p-4">
          <Title_3>{{ plant.name }}</Title_3>
          <Text>Variété: {{ plant.variety.varietyName }}</Text>
          <div class="flex items-center mb-2">
            <SunIcon class="h-5 w-5 text-yellow-500 mr-1" />
            <ProgressBar :value=getLightPourcentage(plant.lightLevel)></ProgressBar>
          </div>
          <div class="flex items-center mb-4">
            <DropletIcon class="h-5 w-5 text-blue-500 mr-1" />
            <ProgressBar color="bg-blue-500" :value=getWaterPourcentage(plant.monthlyWaterFrequency)></ProgressBar>
          </div>
          <div class="flex justify-between">
            <Button @click="editPlant(plant)" class="text-green-600 hover:text-green-800">
              <EditIcon class="h-5 w-5" />
            </button>
            <Button @click="deletePlant(plant)" class="text-red-600 hover:text-red-800"><TrashIcon class="h-5 w-5" /></Button>
          </div>
        </div>
      </div>
    </div>

    <button @click="showAddPlantModal" class="fixed bottom-6 right-6 bg-green-600 hover:bg-green-700 text-white font-bold py-3 px-6 rounded-full shadow-lg transition duration-300">
      <PlusIcon class="h-6 w-6" />
    </button>

    <!-- Modal pour ajouter une plante -->
    <div v-if="isAddModalVisible" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg p-8 w-full max-w-md">
        <h2 class="text-2xl font-bold mb-4">Ajouter une plante</h2>
        <form @submit.prevent="addPlant">
          <div class="mb-4">
            <label for="name" class="block text-gray-700 text-sm font-bold mb-2">Nom</label>
            <input v-model="newPlant.name" type="text" id="name" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" required>
          </div>
          <div class="mb-4">
            <label for="variety" class="block text-gray-700 text-sm font-bold mb-2">Variété</label>
            <input v-model="newPlant.variety" type="text" id="variety" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" required>
          </div>

          <div class="mb-4">
            <label for="luminosity" class="block text-gray-700 text-sm font-bold mb-2">Luminosité</label>
            <select v-model="newPlant.lightLevel" type="string" class="shadow border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" id="luminosity">
              <option value="HIGH">élevé</option>
              <option selected="selected" value="MEDIUM">moyen</option>
              <option value="LOW">faible</option>
            </select>
          </div>
          <div class="mb-4">
            <label for="watering" class="block text-gray-700 text-sm font-bold mb-2">Nombre arrosages mensuel</label>
            <input v-model="newPlant.watering" type="number" id="watering" min="0" max="30" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" required>
          </div>
          <div class="flex justify-end">
            <button type="button" @click="hideAddPlantModal" class="bg-gray-300 hover:bg-gray-400 text-gray-800 font-bold py-2 px-4 rounded mr-2">
              Annuler
            </button>
            <button type="submit" class="bg-green-600 hover:bg-green-700 text-white font-bold py-2 px-4 rounded">
              Ajouter
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- Modal pour éditer une plante -->
    <div v-if="isEditModalVisible" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg p-8 w-full max-w-md">
        <h2 class="text-2xl font-bold mb-4">Éditer une plante</h2>
        <form @submit.prevent="updatePlant">
          <div class="mb-4">
            <label for="edit-name" class="block text-gray-700 text-sm font-bold mb-2">Nom</label>
            <input v-model="editingPlant.name" type="text" id="edit-name" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" required>
          </div>
          <div class="mb-4">
            <label for="edit-variety" class="block text-gray-700 text-sm font-bold mb-2">Variété</label>
            <input v-model="editingPlant.variety.varietyName" type="text" id="edit-variety" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" required>
          </div>
          <div class="mb-4">
            <label for="edit-luminosity" class="block text-gray-700 text-sm font-bold mb-2">Luminosité</label>
            <select v-model="editingPlant.lightLevel" type="string" class="shadow border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" id="edit-luminosity">
              <option value="HIGH">élevé</option>
              <option selected="selected" value="MEDIUM">moyen</option>
              <option value="LOW">faible</option>
            </select>
          </div>
          <div class="mb-4">
            <label for="edit-watering" class="block text-gray-700 text-sm font-bold mb-2">Nombre arrosages mensuel</label>
            <input v-model="editingPlant.monthlyWaterFrequency" type="number" id="edit-watering" min="0" max="30" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" required>
          </div>
          <div class="flex justify-end">
            <button type="button" @click="hideEditPlantModal" class="bg-gray-300 hover:bg-gray-400 text-gray-800 font-bold py-2 px-4 rounded mr-2">
              Annuler
            </button>
            <button type="submit" class="bg-green-600 hover:bg-green-700 text-white font-bold py-2 px-4 rounded">
              Mettre à jour
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref, watchEffect } from 'vue';
import { usePlantStore } from '@/stores/usePlantStore';
import  Button  from '../components/Button.vue';
import  Title_2  from '../components/Title_2.vue';
import  Title_3  from '../components/Title_3.vue';
import  Text     from '../components/Text.vue';
import  ProgressBar     from '../components/ProgressBar.vue';

import { SunIcon, DropletIcon, EditIcon, TrashIcon, PlusIcon, LeafIcon, Loader2Icon } from 'lucide-vue-next';

const plantStore = usePlantStore();
const loading = ref(true);
const error = ref(null);
const isAddModalVisible = ref(false);
const isEditModalVisible = ref(false);
const newPlant = ref({
  name: '',
  variety: '',
  luminosity: '',
  watering: 4
});
const editingPlant = ref(null);
onMounted(async () => {
  console.log("loading")
  loading.value = true;
  try {
    await plantStore.fetchPlants();

    plantStore.plants.forEach(plant => {
      if(plant.lightLevel=="LOW"){
        plant.lightLevel==30
      }else if (plant.lightLevel=="MEDIUM"){
        plant.lightLevel==60
      }else{
        plant.lightLevel==99
      };
    });
    console.log(plantStore.plants)
    loading.value = false;
    console.log("loaded")
  } catch (err) {
    error.value = "Erreur lors du chargement des plantes.";
    loading.value = false;
  }
})


function getLightPourcentage(lightLevel){
  switch (lightLevel){
    case("LOW"):
      return 33;
    case("MEDIUM"):
      return 66;
    case("HIGH"):
      return 100;
    default:
      return 0;
  }
}
function getWaterPourcentage(waterFrequency){
  return Math.max(Math.min(100,(6*waterFrequency+30)),0)
}
const showAddPlantModal = () => {
  isAddModalVisible.value = true;
};

const hideAddPlantModal = () => {
  isAddModalVisible.value = false;
  newPlant.value = { name: '', variety: '', luminosity: '', watering: 4 };
};

const addPlant = async () => {
  try {
    await plantStore.createPlant({
      ...newPlant.value,
      variety: { varietyName: newPlant.value.variety }
    });
    await plantStore.fetchPlants();
    hideAddPlantModal();
  } catch (err) {
    error.value = "Erreur lors de l'ajout de la plante.";
  }
};

const editPlant = (plant) => {
  editingPlant.value = { ...plant };
  isEditModalVisible.value = true;
};

const hideEditPlantModal = () => {
  isEditModalVisible.value = false;
  editingPlant.value = null;
};

const updatePlant = async () => {
  try {
    console.log(editingPlant.value.id)
    await plantStore.createPlant(editingPlant.value);
    await plantStore.fetchPlants();
    hideEditPlantModal();
  } catch (err) {
    error.value = "Erreur lors de la mise à jour de la plante.";
  }
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
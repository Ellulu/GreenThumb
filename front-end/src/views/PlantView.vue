<template>
  <div class="container mx-auto px-4 py-8">
    <Title>Mes Plantes</Title>
    
    <div v-if="loading" class="text-center py-8">
      <Loader2Icon class="animate-spin h-8 w-8 mx-auto text-green-600" />
      <p class="mt-2 text-gray-600">Chargement des plantes...</p>
    </div>

    <Error v-else-if="error">
      <p>{{ error }}</p>
    </Error>

    <div v-else-if="plantStore.plants.length <= 0" class="text-center py-8">
      <LeafIcon class="h-16 w-16 mx-auto text-gray-400" />
      <Text class="text-xl" >Vous n'avez pas encore de plantes.</Text>
      <Button @click="showAddPlantModal" class="mt-4 bg-green-600 hover:bg-green-700 text-white py-2 px-4">
        Ajouter une plante
      </Button>
    </div>

    <div v-else class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
      <ImageBox :alt="plant.name" :image="plant.picture" v-for="plant in plantStore.plants" :key="plant.id">
          <Title_3>{{ plant.name }}</Title_3>
          <Text>Variété: {{ plant.variety.name }}</Text>
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
            <Button @click="deletePlant(plant.id)" class="text-red-600 hover:text-red-800"><TrashIcon class="h-5 w-5" /></Button>
          </div>
      </ImageBox>
    </div>

    <button @click="showAddPlantModal" class="fixed bottom-6 sm:bottom-24 right-6 bg-green-600 hover:bg-green-700 text-white font-bold py-3 px-6 rounded-full shadow-lg transition duration-300">
      <PlusIcon class="h-6 w-6" />
    </button>

    <!-- Modal pour ajouter une plante -->
      <FormBox 
      v-if="isAddModalVisible"
      title="Ajouter une plante" 
      submitText="Ajouter" 
      cancelText="Annuler"
      @submit="addPlant" 
      @cancel="hideAddPlantModal"
      >
      <Input 
        name="Nom" 
        v-model="newPlant.name"
        type="text" 
        placeholder="Nom de la plante" 
        required 
      />
      <Input 
        name="Variété" 
        v-model="newPlant.variety"
        type="text" 
        placeholder="Variété" 
        required 
      />
      <Select
        name="Luminosité"
        v-model="newPlant.lightLevel"
        :options="lightLevelOptions" 
        required
      />
      <Input 
      name="Nombre arrosages mensuel" 
      v-model="newPlant.monthlyWaterFrequency"
      type="number" 
      placeholder="Nombre d'arrosages mensuel" 
      required 
      min="0" 
      max="30" 
    />
    <input
      type="file"
      @change="getPlantPicture"
      class="block w-full text-sm text-slate-500 file:py-2 file:px-4 file:rounded-md file:border-0 file:text-sm file:font-semibold file:bg-green-100 file:text-green-700 hover:file:bg-green-200 hover:file:cursor-pointer"
    />
    </FormBox>

    <!-- Modal pour éditer une plante -->
    <FormBox 
    v-if="isEditModalVisible"
    title="Éditer une plante" 
    submitText="Mettre à jour" 
    cancelText="Annuler"
    @submit="updatePlant" 
    @cancel="hideEditPlantModal"
  >
    <Input 
      name="Nom" 
      v-model="editingPlant.name"
      type="text" 
      placeholder="Nom de la plante" 
      required 
    />
    <Input 
      name="Variété" 
      v-model="editingPlant.variety.name"
      type="text" 
      placeholder="Variété" 
      required 
    />
    <Select
      name="Luminosité"
      v-model="editingPlant.lightLevel"
      :options="lightLevelOptions" 
      required 
    />
    <Input 
      name="Nombre arrosages mensuel" 
      v-model="editingPlant.monthlyWaterFrequency"
      type="number" 
      placeholder="Nombre d'arrosages mensuel" 
      required 
      min="0" 
      max="30" 
    />
    <input
      type="file"
      @change="getEditingPlantPicture"
      class="block w-full text-sm text-slate-500 file:py-2 file:px-4 file:rounded-md file:border-0 file:text-sm file:font-semibold file:bg-green-100 file:text-green-700 hover:file:bg-green-200 hover:file:cursor-pointer"
    />
  </FormBox>
  </div>
</template>

<script setup>
import { onMounted, ref, watchEffect } from 'vue';
import { usePlantStore } from '@/stores/usePlantStore';
import { Button, Title, Title_3, Text, ProgressBar, ImageBox, FormBox, Input, Select, Error } from '../components';
import { SunIcon, DropletIcon, EditIcon, TrashIcon, PlusIcon, LeafIcon, Loader2Icon } from 'lucide-vue-next';

const lightLevelOptions = [
  { value: 'HIGH', label: 'élevé' },
  { value: 'MEDIUM', label: 'moyen' },
  { value: 'LOW', label: 'faible' }
]
const plantStore = usePlantStore();
const loading = ref(true);
const error = ref(null);
const isAddModalVisible = ref(false);
const isEditModalVisible = ref(false);
const newPlant = ref({
  name: '',
  variety: '',
  lightLevel : '',
  monthlyWaterFrequency: 4,
  picture: null
});
const editingPlant = ref(null);
onMounted(async () => {
  loading.value = true;
  try {
    await plantStore.fetchPlants();
    console.log("plants",plantStore.plants)
    plantStore.plants.forEach(plant => {
      if(plant.lightLevel=="LOW"){
        plant.lightLevel==30
      }else if (plant.lightLevel=="MEDIUM"){
        plant.lightLevel==60
      }else{
        plant.lightLevel==99
      };
    });
    loading.value = false;
  } catch (err) {
    error.value = "Erreur lors du chargement des plantes.";
    loading.value = false;
  }
})
function getEditingPlantPicture(event) {
  editingPlant.value.picture = event.target.files[0];
  if (editingPlant.value.picture) {
    const reader = new FileReader();
    reader.readAsDataURL(editingPlant.value.picture);
  }
}
function getPlantPicture(event) {
    newPlant.value.picture = event.target.files[0];
    if (newPlant.value.picture) {
        const reader = new FileReader();
        reader.readAsDataURL(newPlant.value.picture);
    }
}

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
  newPlant.value = { name: '', variety: '', lightLevel:'', monthlyWaterFrequency: 4 };
};
const addPlant = async () => {
  try {
    await plantStore.createPlant({
      id: 0,
      name: newPlant.value.name,
      variety:{
        name:newPlant.value.variety
      },
      lightLevel: newPlant.value.lightLevel,
      monthlyWaterFrequency: newPlant.value.monthlyWaterFrequency,
    },newPlant.value.picture);
    await plantStore.fetchPlants();
    hideAddPlantModal();
  } catch (err) {
    console.log(err)
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
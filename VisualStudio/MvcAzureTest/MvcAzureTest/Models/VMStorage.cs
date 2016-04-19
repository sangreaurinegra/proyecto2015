using Microsoft.WindowsAzure.Storage;
using Microsoft.WindowsAzure.Storage.Blob;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace MvcAzureTest.Models
{
    public class VMStorage
    {
        public string NombreCuenta { get; set; }

        public string ClaveCuenta { get; set; }

        public string Contenedor { get; set; }

        public List<SelectListItem> Contenedores { get; set; }

        public HttpPostedFileBase Archivo { get; set; }



        internal void LoadContenedores()
        {
            CloudStorageAccount storageAccount = CloudStorageAccount.Parse("DefaultEndpointsProtocol=https;AccountName=" + NombreCuenta + ";AccountKey=" + ClaveCuenta);
            CloudBlobClient blobClient = storageAccount.CreateCloudBlobClient();
            IEnumerable<CloudBlobContainer> blobContainers = blobClient.ListContainers();
            Contenedores = new List<SelectListItem>();
            foreach(CloudBlobContainer cont in blobContainers)
            {
                Contenedores.Add(new SelectListItem() { Value = cont.Name, Text = cont.Name });
            }
        }

        internal void SubirArchivo()
        {
            CloudStorageAccount storageAccount = CloudStorageAccount.Parse("DefaultEndpointsProtocol=https;AccountName=" + NombreCuenta + ";AccountKey=" + ClaveCuenta);
            CloudBlobClient blobClient = storageAccount.CreateCloudBlobClient();
            CloudBlobContainer container = blobClient.GetContainerReference(Contenedor);
            CloudBlockBlob blockBlob = container.GetBlockBlobReference(Archivo.FileName);
            blockBlob.UploadFromStream(Archivo.InputStream);
        }
    }
}
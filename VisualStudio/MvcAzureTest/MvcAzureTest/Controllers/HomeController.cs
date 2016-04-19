using Microsoft.WindowsAzure.Storage;
using Microsoft.WindowsAzure.Storage.Blob;
using MvcAzureTest.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace MvcAzureTest.Controllers
{
    public partial class HomeController : Controller
    {
        
        public virtual ActionResult Index()
        {
            ActionResult result;
            try
            {
                MvcAzureTest.Models.VMStorage vmStorage = new Models.VMStorage();
                result = View(MVC.Home.Views.ViewNames.Index, vmStorage);
                
            }
            catch (Exception ex)
            {
                return Content(ex.ToString());
            }

            return result;
        }

        public virtual ActionResult Subir(VMStorage vmStorage)
        {
            ActionResult result = null;
            try
            {
                vmStorage.SubirArchivo();
                result = View(MVC.Home.Views.ViewNames.Index, vmStorage);
            }
            catch (Exception ex)
            {
                result = Content(ex.ToString());
            }
            return result;
        }

        public virtual ActionResult IngresarCuenta(VMStorage vmStorage)
        {
            ActionResult result = null;
            try
            {
                vmStorage.LoadContenedores();
                result = View(MVC.Home.Views.ViewNames.Subir, vmStorage);
            }
            catch (Exception ex)
            {
                Content(ex.ToString());
            }
            return result;
        }

    }
}

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Windows.Networking.BackgroundTransfer;
using Windows.Storage;
using Folder = Windows.Storage.StorageFolder;

namespace AccounterNative
{
    public sealed class Downloader
    {
        public async void download(String url, Folder folder)
        {

            var uri = new Uri(url);
            var downloader = new BackgroundDownloader();
            StorageFile file = await folder.CreateFileAsync("100MB.zip",
                CreationCollisionOption.ReplaceExisting);
            DownloadOperation download = downloader.CreateDownload(uri, file);
            download.StartAsync();
        }
    }
}

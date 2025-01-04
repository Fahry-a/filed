import yt_dlp

def download_video(url, location, file_format):
    ydl_opts = {
        'noplaylist': True,
        'outtmpl': f'{location}/%(title)s.%(ext)s',
        'postprocessors': [{'key': 'FFmpegMetadata'}]  # Tambahkan metadata ke file
    }

    if file_format == 'mp4':
        ydl_opts['format'] = 'bestvideo[ext=mp4][height<=720]+bestaudio[ext=m4a]/best[ext=mp4][height<=720]/best'
    elif file_format == 'mp3':
        ydl_opts['format'] = 'bestaudio'
        ydl_opts['postprocessors'].insert(0, {
            'key': 'FFmpegExtractAudio',
            'preferredcodec': 'mp3',
            'preferredquality': '320'
        })

    try:
        with yt_dlp.YoutubeDL(ydl_opts) as ydl:
            ydl.download([url])
        print("Pengunduhan selesai.")
    except Exception as e:
        print(f"Kesalahan: {e}")

def main():
    video_url = input("Masukkan URL video YouTube: ").strip()
    location = input("Masukkan lokasi penyimpanan: ").strip()
    file_format = input("Pilih format (mp4/mp3): ").strip().lower()

    while file_format not in ['mp4', 'mp3']:
        file_format = input("Pilih format yang valid (mp4/mp3): ").strip().lower()

    if video_url and location:
        download_video(video_url, location, file_format)
    else:
        print("URL atau lokasi tidak valid.")

if __name__ == "__main__":
    main()
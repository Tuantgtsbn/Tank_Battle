package tankfighter;

import javax.sound.sampled.*;

import Resources.ResourceGetter;

public class Sound {
	private Clip clip;// để phát âm thanh
	private FloatControl volumeControl; // điều khiển âm thanh
	// Thay "soundFileName" bằng tên file âm thanh của bạn

	public Sound(String soundFileName) {
		try {
			// Lấy đối tượng âm thanh
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(ResourceGetter.class.getResource(soundFileName));
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			// Tạo 1 đối tượng kiểu FloatControl để điều chỉnh âm thanh
			volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			// Thêm LineListener để theo dõi sự kiện kết thúc âm thanh
			clip.addLineListener(new LineListener() {
				@Override
				public void update(LineEvent event) {
					if (event.getType() == LineEvent.Type.STOP) {
						clip.setFramePosition(0); // Khi kết thúc, quay lại đầu
						clip.start();
					}
				}
			});

		} catch (Exception e) {
			System.out.println("Không thể mở file âm thanh: " + soundFileName);
			e.printStackTrace();
		}
	}

	public void play() {
		if (clip != null) {
			clip.setFramePosition(0);
			decreaseVolume(-10.0f);
			clip.start();
		}
	}

	public void stop() {
		if (clip != null) {
			clip.stop();
		}
	}

	// Phương thức giảm âm lượng (volume là giá trị âm lượng mới, ví dụ: -10.0f)
	public void decreaseVolume(float volume) {
		if (volumeControl != null) {
			float currentVolume = volumeControl.getValue();
			volumeControl.setValue(currentVolume + volume);
		}
	}
}

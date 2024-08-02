import pyrealsense2 as rs
import ntcore
import json

class T265Manager:
    def __init__(self, device_id=None):
        self.pipeline = rs.pipeline()
        config = rs.config()
        if device_id:
            config.enable_device(device_id)

        config.enable_stream(rs.stream.pose)
        self.pipeline.start(config)
        self.table = self.start_nt_and_get_table(device_id)

    def start_nt_and_get_table(self, name):
        instance = ntcore.NetworkTableInstance.getDefault()
        instance.setServer("localhost")
        table_name = f"T265"
        if name:
            table_name += f"/{name}"
        instance.startClient4(name)
        return instance.getTable(table_name)

    def run(self):
        frames = self.pipeline.wait_for_frames()
        pose_frame = frames.get_pose_frame()
        if not pose_frame:
            return

        data = self.format_pose_frame(pose_frame)
        self.table.putString("jsonDump", json.dumps(data))
        self.table.putNumber("confidence", data["confidence"])

    @staticmethod
    def format_pose_frame(pose_frame):
        data = pose_frame.get_pose_data()
        confidence = data.tracker_confidence
        return {
            "translation": [
                data.translation.x,
                data.translation.y,
                data.translation.z
            ],
            "rotation": [
                data.rotation.w,
                data.rotation.x,
                data.rotation.y,
                data.rotation.z
            ],
            "confidence": confidence
        }


if __name__ == "__main__":
    t265_manager = T265Manager("908412110743")
    while True:
        t265_manager.run()

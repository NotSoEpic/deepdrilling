import json
from pathlib import Path


def generate(resources_path: Path, extra_path: Path, entry: dict):
    file_path = (resources_path / extra_path).with_suffix(".json")
    file_path.parent.mkdir(parents=True, exist_ok=True)
    with open(file_path, "w") as f:
        json.dump(entry, f, indent=1)

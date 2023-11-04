class Adaptation {
    constructor(name, state, settings) {
        this._name = name;
        this._state = state;
        this._settings = settings;
    }

    set name(value) {
        this._name = value;
    }

    set state(value) {
        this._state = value;
    }

    set settings(value) {
        this._settings = value;
    }

    get name() {
        return this._name;
    }

    get state() {
        return this._state;
    }

    get settings() {
        return this._settings;
    }
}
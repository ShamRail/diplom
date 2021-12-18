let host = "http://localhost:8080/"

async function authFetch(url) {
    let pass = localStorage.getItem('user')
    return await fetch(url, {
        headers: {
            "Authorization": "Basic " + pass
        }
    }).then(resp => {
        if (resp.ok) {
            resp.json()
        } else {
            alert("Not authorized")
        }
    });
}

async function GetProjectDescriptionAll() {
    let url = "project_description/"
    let response = await fetch(host + url + "all");
    if (response.ok){
        return await response.json()
    }
}

async function GetProjectDescriptionByProjectId(project_id){
    let url = host + "project_description/filter?project_id=" + project_id
    let response = await fetch(url);
    if (response.ok){
        return await response.json()
    }
}

async function GetProjectDocById(id) {
    let url = host + "project_docs?id=" + id
    let response = await fetch(url);
    if (response.ok){
        return await response.json()
    }
}

async function GetUser(email, password){
    let url = host + "users?email=" + email
    let pass = btoa(email + ":"+password)
    let response = await fetch(url, {
        headers: {
            "Authorization": "Basic " + pass
        }
    });
    return response
}
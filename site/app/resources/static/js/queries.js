let host = "http://localhost:8084/app/";
let hostRunner =  "http://localhost:8084/runner/app/";

// let serviceHostApp = "apprunner.ugatu.su/app/"
// let serviceHostRunnerApp = "http://apprunner.ugatu.su/runner/app/"

async function AuthGetFetch(url) {
    let pass = localStorage.getItem('user')
    return await fetch(url, {
        headers: {
            "Authorization": "Basic " + pass
        }
    }).then(resp => resp);
}

async function GetProjectDescriptionAll() {
    let url = "project_description/"
    let response = await fetch(host + url + "all");
    if (response.ok) {
        return await response.json()
    }
}

async function GetProjectDescriptionByProjectId(project_id) {
    let url = host + "project_description/filter?project_id=" + project_id
    let response = await fetch(url);
    if (response.ok) {
        return await response.json()
    }
}

async function GetProjectDocById(id) {
    let url = host + "project_docs?id=" + id
    let response = await fetch(url);
    if (response.ok) {
        return await response.json()
    }
}

async function GetProjectDocsByUserId(id) {
    let url = host + "/project_doc/user?userId=" + id
    return await AuthGetFetch(url);
}

async function GetUser(email, password) {
    let url = host + "users?email=" + email
    let pass = btoa(email + ":" + password)
    let response = await fetch(url, {
        headers: {
            "Authorization": "Basic " + pass
        }
    });
    return response
}

async function DeleteGetProjectDocs(intent) {
    let url = host + "delete_project_doc"
    let pass = localStorage.getItem('user')
    return await fetch(url, {
        headers: {
            "Authorization": "Basic " + pass
        },
        method: "DELETE",
        body: JSON.stringify(intent)
    })
}

async function CreateProject(intent){
    let url = host + "project_doc"
    let pass = localStorage.getItem('user')
    return await fetch(url, {
        headers: {
            "Authorization": "Basic " + pass
        },
        method: "POST",
        body: JSON.stringify(intent)
    })
}

async function UpdateProjectDoc(intent){
    let url = host + "project_doc"
    let pass = localStorage.getItem('user')
    return await fetch(url, {
        headers: {
            "Authorization": "Basic " + pass
        },
        method: "PUT",
        body: JSON.stringify(intent)
    })
}

async function GetAllConfiguration(){
    return AuthGetFetch(host + "configuration/all")
}

async function  GetStatusProject(id){
    // return AuthGetFetch(host + "status?id=" + id)
    return await fetch(host + "status?id=" + id).then(resp => resp);
}

async function BuildProject(projectId){
    let formData = new FormData();
    formData.append('id', projectId);
    let url = host + "build"
    let pass = localStorage.getItem('user')
    return await fetch(url, {
        headers: {
            "Authorization": "Basic " + pass
        },
        method: "POST",
        body: formData
    })
}

async function CreateUser(intent, email, password){
    let url = host + "users"
    let t = btoa(email + ":"+password)
    return await fetch(url, {
        headers: {
            "Authorization": "Basic " + t
        },
        method: "POST",
        body: JSON.stringify(intent)
    })
}